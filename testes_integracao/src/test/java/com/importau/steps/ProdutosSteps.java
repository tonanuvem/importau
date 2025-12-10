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

public class ProdutosSteps {
    
    private static final String BASE_URL = "http://localhost:8001";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private Response response;
    private String produtoId;
    private long startTime;

    @Dado("que o microsserviço de produtos está disponível")
    public void verificarServicoDisponivel() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/status")
                .build();
        
        response = client.newCall(request).execute();
        Assertions.assertThat(response.code()).isEqualTo(200);
    }

    @Quando("solicito a lista de produtos")
    public void solicitarListaProdutos() throws IOException {
        startTime = System.currentTimeMillis();
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos")
                .build();
        
        response = client.newCall(request).execute();
    }

    @Então("devo receber status {int}")
    public void verificarStatus(int expectedStatus) {
        Assertions.assertThat(response.code()).isEqualTo(expectedStatus);
    }

    @E("devo receber uma lista de produtos")
    public void verificarListaProdutos() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.isArray()).isTrue();
    }

    @Dado("que tenho os dados de um novo produto:")
    public void definirDadosProduto(DataTable dataTable) {
        // Os dados serão usados no próximo step
        List<Map<String, String>> rows = dataTable.asMaps(String.class, String.class);
        // Armazena dados para uso posterior
    }

    @Quando("crio o produto")
    public void criarProduto() throws IOException {
        String produtoJson = "{\n" +
            "    \"codigo\": \"TEST001\",\n" +
            "    \"nome\": \"Produto Teste\",\n" +
            "    \"categoria\": \"Teste\",\n" +
            "    \"preco_unitario_brl\": 100.0\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                produtoJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos")
                .post(body)
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("o produto deve ser criado com sucesso")
    public void verificarProdutoCriado() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.has("id")).isTrue();
        Assertions.assertThat(jsonNode.get("codigo").asText()).isEqualTo("TEST001");
        
        // Armazena ID para testes posteriores
        produtoId = jsonNode.get("id").asText();
    }

    @Dado("que existe um produto com codigo {string}")
    public void criarProdutoExistente(String codigo) throws IOException {
        // Primeiro verifica se já existe
        Request getRequest = new Request.Builder()
                .url(BASE_URL + "/produtos")
                .build();
        
        Response getResponse = client.newCall(getRequest).execute();
        String responseBody = getResponse.body().string();
        JsonNode produtos = objectMapper.readTree(responseBody);
        
        // Procura produto existente
        for (JsonNode produto : produtos) {
            if (produto.get("codigo").asText().equals(codigo)) {
                produtoId = produto.get("id").asText();
                return;
            }
        }
        
        // Se não existe, cria um novo
        String produtoJson = "{\n" +
            "    \"codigo\": \"" + codigo + "\",\n" +
            "    \"nome\": \"Produto " + codigo + "\",\n" +
            "    \"categoria\": \"Teste\",\n" +
            "    \"preco_unitario_brl\": 100.0\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                produtoJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos")
                .post(body)
                .build();
        
        Response createResponse = client.newCall(request).execute();
        String createResponseBody = createResponse.body().string();
        JsonNode createdProduto = objectMapper.readTree(createResponseBody);
        produtoId = createdProduto.get("id").asText();
    }

    @Quando("busco o produto pelo ID")
    public void buscarProdutoPorId() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos/" + produtoId)
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("devo receber os dados do produto")
    public void verificarDadosProduto() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.has("id")).isTrue();
        Assertions.assertThat(jsonNode.has("codigo")).isTrue();
        Assertions.assertThat(jsonNode.has("nome")).isTrue();
    }

    @Quando("atualizo o nome para {string}")
    public void atualizarNomeProduto(String novoNome) throws IOException {
        String updateJson = "{\n" +
            "    \"nome\": \"" + novoNome + "\"\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                updateJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos/" + produtoId)
                .put(body)
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("o produto deve ter o nome atualizado")
    public void verificarNomeAtualizado() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.get("nome").asText()).isEqualTo("Produto Atualizado");
    }

    @Quando("excluo o produto")
    public void excluirProduto() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos/" + produtoId)
                .delete()
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("o produto deve ser removido")
    public void verificarProdutoRemovido() throws IOException {
        // Verifica se produto foi removido tentando buscá-lo
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos/" + produtoId)
                .build();
        
        Response getResponse = client.newCall(request).execute();
        Assertions.assertThat(getResponse.code()).isEqualTo(404);
    }

    @Dado("que existem produtos das categorias {word} e {word}")
    public void criarProdutosCategorias(String categoria1, String categoria2) throws IOException {
        // Cria produto da primeira categoria
        criarProdutoCategoria("CATTEST1", categoria1);
        // Cria produto da segunda categoria  
        criarProdutoCategoria("CATTEST2", categoria2);
    }

    @Quando("filtro produtos pela categoria {word}")
    public void filtrarPorCategoria(String categoria) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos?categoria=" + categoria)
                .build();
        
        response = client.newCall(request).execute();
    }

    @Então("devo receber apenas produtos da categoria {word}")
    public void verificarFiltroCategoria(String categoria) throws IOException {
        String responseBody = response.body().string();
        JsonNode produtos = objectMapper.readTree(responseBody);
        
        for (JsonNode produto : produtos) {
            Assertions.assertThat(produto.get("categoria").asText()).isEqualTo(categoria);
        }
    }

    @Então("o tempo de resposta deve ser menor que {int} segundos")
    public void verificarTempoResposta(int maxSeconds) {
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        
        Assertions.assertThat(responseTime).isLessThan(maxSeconds * 1000L);
    }

    @Quando("acesso o endpoint de saúde")
    public void acessarEndpointSaude() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/status")
                .build();
        
        response = client.newCall(request).execute();
    }

    @E("devo receber confirmação de que o serviço está saudável")
    public void verificarServicoSaudavel() throws IOException {
        String responseBody = response.body().string();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Assertions.assertThat(jsonNode.get("status").asText()).isEqualTo("healthy");
        Assertions.assertThat(jsonNode.get("service").asText()).isEqualTo("produtos");
    }

    // Método auxiliar
    private void criarProdutoCategoria(String codigo, String categoria) throws IOException {
        String produtoJson = "{\n" +
            "    \"codigo\": \"" + codigo + "\",\n" +
            "    \"nome\": \"Produto " + categoria + "\",\n" +
            "    \"categoria\": \"" + categoria + "\",\n" +
            "    \"preco_unitario_brl\": 100.0\n" +
            "}";
        
        RequestBody body = RequestBody.create(
                produtoJson, 
                MediaType.get("application/json; charset=utf-8")
        );
        
        Request request = new Request.Builder()
                .url(BASE_URL + "/produtos")
                .post(body)
                .build();
        
        client.newCall(request).execute();
    }
}
