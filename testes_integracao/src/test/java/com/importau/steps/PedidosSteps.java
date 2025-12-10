package com.importau.steps;

import io.cucumber.java.pt.*;
import io.cucumber.datatable.DataTable;
import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.assertj.core.api.Assertions;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PedidosSteps {
    
    private static final String BASE_URL = "http://localhost:8002";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private Response response;
    private String pedidoId;
    private long startTime;

    @Dado("que o microsserviço de pedidos está disponível")
    public void verificarServicoDisponivel() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/status")
                .build();
        
        response = client.newCall(request).execute();
        Assertions.assertThat(response.code()).isEqualTo(200);
    }

    @Quando("solicito a lista de pedidos")
    public void solicitarListaPedidos() throws IOException {
        startTime = System.currentTimeMillis();
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos")
                .build();
        
        response = client.newCall(request).execute();
    }

    @Então("devo receber uma lista de pedidos")
    public void verificarListaPedidos() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.isArray()).isTrue();
    }

    @Dado("que tenho os dados de um novo pedido:")
    public void definirDadosPedido(DataTable dataTable) {
        // Os dados serão usados no próximo step
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        // Armazena dados para uso posterior
    }

    @Quando("crio o pedido")
    public void criarPedido() throws IOException {
        String pedidoJson = "{\n" +
            "    \"pedido_id\": \"PED999\",\n" +
            "    \"data_pedido\": \"2024-12-10\",\n" +
            "    \"fornecedor_id\": \"FORN001\",\n" +
            "    \"valor_total_brl\": 2500.00,\n" +
            "    \"status\": \"PENDENTE\",\n" +
            "    \"tipo_pagamento\": \"BOLETO\",\n" +
            "    \"usuario_criacao\": \"test.user\"\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                pedidoJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos")
                .post(body)
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("o pedido deve ser criado com sucesso")
    public void verificarPedidoCriado() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.has("id")).isTrue();
        Assertions.assertThat(jsonNode.get("pedido_id").asText()).isEqualTo("PED999");
        
        // Armazena ID para testes posteriores
        pedidoId = jsonNode.get("id").asText();
    }

    @Dado("que existe um pedido com codigo {string}")
    public void criarPedidoExistente(String codigo) throws IOException {
        // Primeiro verifica se já existe
        Request getRequest = new Request.Builder()
                .url(BASE_URL + "/pedidos")
                .build();
        
        Response getResponse = client.newCall(getRequest).execute();
        String responseBody = getResponse.body().string();
        JsonNode pedidos = objectMapper.readTree(responseBody);
        
        // Procura pedido existente
        for (JsonNode pedido : pedidos) {
            if (pedido.get("pedido_id").asText().equals(codigo)) {
                pedidoId = pedido.get("id").asText();
                return;
            }
        }
        
        // Se não existe, cria um novo
        String pedidoJson = "{\n" +
            "    \"pedido_id\": \"" + codigo + "\",\n" +
            "    \"data_pedido\": \"2024-12-10\",\n" +
            "    \"fornecedor_id\": \"FORN001\",\n" +
            "    \"valor_total_brl\": 1500.00,\n" +
            "    \"status\": \"PENDENTE\"\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                pedidoJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos")
                .post(body)
                .build();
        
        Response createResponse = client.newCall(request).execute();
        String createResponseBody = createResponse.body().string();
        JsonNode createdPedido = objectMapper.readTree(createResponseBody);
        pedidoId = createdPedido.get("id").asText();
    }

    @Quando("busco o pedido pelo ID")
    public void buscarPedidoPorId() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos/" + pedidoId)
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("devo receber os dados do pedido")
    public void verificarDadosPedido() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.has("id")).isTrue();
        Assertions.assertThat(jsonNode.has("pedido_id")).isTrue();
        Assertions.assertThat(jsonNode.has("valor_total_brl")).isTrue();
    }

    @Quando("atualizo o status para {string}")
    public void atualizarStatusPedido(String novoStatus) throws IOException {
        String updateJson = "{\n" +
            "    \"status\": \"" + novoStatus + "\"\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                updateJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos/" + pedidoId)
                .put(body)
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("o pedido deve ter o status atualizado")
    public void verificarStatusAtualizado() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.get("status").asText()).isEqualTo("EM_TRANSITO");
    }

    @Quando("excluo o pedido")
    public void excluirPedido() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos/" + pedidoId)
                .delete()
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("o pedido deve ser removido")
    public void verificarPedidoRemovido() throws IOException {
        // Verifica se pedido foi removido tentando buscá-lo
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos/" + pedidoId)
                .build();
        
        Response getResponse = client.newCall(request).execute();
        Assertions.assertThat(getResponse.code()).isEqualTo(404);
    }

    @Dado("que existem pedidos com status {word} e {word}")
    public void criarPedidosStatus(String status1, String status2) throws IOException {
        // Cria pedido com primeiro status
        criarPedidoComStatus("STAT1", status1);
        // Cria pedido com segundo status
        criarPedidoComStatus("STAT2", status2);
    }

    @Quando("filtro pedidos pelo status {word}")
    public void filtrarPorStatus(String status) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos?status=" + status)
                .build();
        
        response = client.newCall(request).execute();
    }

    @Então("devo receber apenas pedidos com status {word}")
    public void verificarFiltroStatus(String status) throws IOException {
        String responseBody = response.body().string();
        JsonNode pedidos = objectMapper.readTree(responseBody);
        
        for (JsonNode pedido : pedidos) {
            Assertions.assertThat(pedido.get("status").asText()).isEqualTo(status);
        }
    }

    @Quando("solicito as estatísticas dos pedidos")
    public void solicitarEstatisticas() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos/stats/resumo")
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("devo receber resumo por status")
    public void verificarResumoStatus() throws IOException {
        String responseBody = response.body().string();
        JsonNode stats = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(stats.isArray()).isTrue();
        if (stats.size() > 0) {
            JsonNode firstStat = stats.get(0);
            Assertions.assertThat(firstStat.has("status")).isTrue();
            Assertions.assertThat(firstStat.has("quantidade")).isTrue();
            Assertions.assertThat(firstStat.has("valor_total")).isTrue();
        }
    }

    @Quando("acesso o endpoint de saúde dos pedidos")
    public void acessarEndpointSaudePedidos() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/status")
                .build();
        
        response = client.newCall(request).execute();
    }

    @Então("devo receber status {int}")
    public void verificarStatus(int expectedStatus) {
        Assertions.assertThat(response.code()).isEqualTo(expectedStatus);
    }

    @Então("o tempo de resposta deve ser menor que {int} segundos")
    public void verificarTempoResposta(int maxSeconds) {
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        Assertions.assertThat(responseTime).isLessThan(maxSeconds * 1000L);
    }

    @E("devo receber confirmação de que o serviço está saudável")
    public void verificarServicoSaudavel() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.get("status").asText()).isEqualTo("healthy");
        Assertions.assertThat(jsonNode.get("service").asText()).isEqualTo("pedidos");
    }

    // Método auxiliar
    private void criarPedidoComStatus(String codigo, String status) throws IOException {
        String pedidoJson = "{\n" +
            "    \"pedido_id\": \"" + codigo + "\",\n" +
            "    \"data_pedido\": \"2024-12-10\",\n" +
            "    \"fornecedor_id\": \"FORN001\",\n" +
            "    \"valor_total_brl\": 1000.00,\n" +
            "    \"status\": \"" + status + "\"\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                pedidoJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/pedidos")
                .post(body)
                .build();
        
        client.newCall(request).execute();
    }
}
