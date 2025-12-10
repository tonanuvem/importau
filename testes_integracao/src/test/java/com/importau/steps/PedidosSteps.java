package com.importau.steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

public class PedidosSteps {
    
    private String baseUrl;
    private Response response;
    private Map<String, Object> pedidoData = new HashMap<>();
    private String pedidoUuid;
    
    @Dado("que o microsserviço de pedidos está disponível em {string}")
    public void configurarBaseUrl(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;
    }
    
    @Quando("solicito a lista de pedidos")
    public void solicitarListaPedidos() {
        response = RestAssured.get("/pedidos");
        CommonSteps.setResponse(response);
    }
    
    @Então("devo receber uma lista com pelo menos {int} pedido")
    public void verificarListaNaoVazia(int minimo) {
        assertTrue(response.jsonPath().getList("$").size() >= minimo);
    }
    
    @Dado("que existem pedidos cadastrados")
    public void verificarPedidosExistem() {
        response = RestAssured.get("/pedidos");
        assertTrue(response.jsonPath().getList("$").size() > 0);
    }
    
    @Quando("filtro pedidos pelo status {string}")
    public void filtrarPorStatus(String status) {
        response = RestAssured.given()
            .queryParam("status", status)
            .get("/pedidos");
        CommonSteps.setResponse(response);
    }
    
    @Então("todos os pedidos devem ter status {string}")
    public void verificarStatus(String status) {
        response.then().body("status", everyItem(equalTo(status)));
    }
    
    @Dado("que tenho os dados de um novo pedido:")
    public void prepararDadosPedido(Map<String, String> dados) {
        pedidoData.clear();
        pedidoData.put("pedido_id", dados.get("pedido_id"));
        pedidoData.put("fornecedor_id", dados.get("fornecedor_id"));
        pedidoData.put("valor_total_brl", dados.get("valor_total_brl"));
        pedidoData.put("status", dados.get("status"));
        pedidoData.put("tipo_pagamento", dados.get("tipo_pagamento"));
        pedidoData.put("data_pedido", "2025-12-10");
        pedidoData.put("prazo_dias", 30);
    }
    
    @Quando("crio o pedido")
    public void criarPedido() {
        response = RestAssured.given()
            .contentType("application/json")
            .body(pedidoData)
            .post("/pedidos");
        CommonSteps.setResponse(response);
        
        if (response.getStatusCode() == 201) {
            pedidoUuid = response.jsonPath().getString("id");
        }
    }
    
    @Então("o pedido deve conter o pedido_id {string}")
    public void verificarPedidoId(String pedidoId) {
        assertEquals(pedidoId, response.jsonPath().getString("pedido_id"));
    }
    
    @Dado("que existe um pedido com pedido_id {string}")
    public void buscarPedidoPorId(String pedidoId) {
        response = RestAssured.given()
            .queryParam("pedido_id", pedidoId)
            .get("/pedidos");
        
        if (response.jsonPath().getList("$").size() > 0) {
            pedidoUuid = response.jsonPath().getString("[0].id");
        }
    }
    
    @Quando("busco o pedido pelo pedido_id")
    public void buscarPedido() {
        response = RestAssured.get("/pedidos/" + pedidoUuid);
        CommonSteps.setResponse(response);
    }
    
    @Então("o pedido deve ter status {string}")
    public void verificarStatusUnico(String status) {
        assertEquals(status, response.jsonPath().getString("status"));
    }
    
    @Então("o pedido deve ter valor_total_brl {double}")
    public void verificarValorTotal(double valor) {
        assertEquals(valor, response.jsonPath().getDouble("valor_total_brl"), 0.01);
    }
    
    @Quando("atualizo o status para {string}")
    public void atualizarStatus(String novoStatus) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("status", novoStatus);
        
        response = RestAssured.given()
            .contentType("application/json")
            .body(updateData)
            .put("/pedidos/" + pedidoUuid);
        CommonSteps.setResponse(response);
    }
    
    @Quando("solicito as estatísticas dos pedidos")
    public void solicitarEstatisticas() {
        response = RestAssured.get("/pedidos/stats");
        CommonSteps.setResponse(response);
    }
    
    @Então("a resposta deve conter contadores por status")
    public void verificarEstatisticas() {
        assertNotNull(response.jsonPath().get("total"));
    }
    
    @Quando("filtro pedidos pelo tipo_pagamento {string}")
    public void filtrarPorTipoPagamento(String tipo) {
        response = RestAssured.given()
            .queryParam("tipo_pagamento", tipo)
            .get("/pedidos");
        CommonSteps.setResponse(response);
    }
    
    @Então("todos os pedidos devem ter tipo_pagamento {string}")
    public void verificarTipoPagamento(String tipo) {
        response.then().body("tipo_pagamento", everyItem(equalTo(tipo)));
    }
}
