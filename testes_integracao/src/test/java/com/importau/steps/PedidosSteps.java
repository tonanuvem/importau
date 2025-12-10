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
    
    @Quando("solicito as estatísticas dos pedidos")
    public void solicitarEstatisticas() {
        response = RestAssured.get("/pedidos/stats");
        CommonSteps.setResponse(response);
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
