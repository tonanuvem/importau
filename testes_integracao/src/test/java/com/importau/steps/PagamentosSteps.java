package com.importau.steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class PagamentosSteps {
    
    private String baseUrl;
    private Response response;
    private String pagamentoId;
    
    @Dado("que o microsserviço de pagamentos está disponível em {string}")
    public void configurarBaseUrl(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;
    }
    
    @Quando("solicito a lista de pagamentos")
    public void solicitarListaPagamentos() {
        response = RestAssured.get("/pagamentos");
    }
    
    @Então("devo receber uma lista com pelo menos {int} pagamento")
    public void verificarListaNaoVazia(int minimo) {
        assertTrue(response.jsonPath().getList("$").size() >= minimo);
    }
    
    @Dado("que existem pagamentos cadastrados")
    public void verificarPagamentosExistem() {
        response = RestAssured.get("/pagamentos");
        assertTrue(response.jsonPath().getList("$").size() > 0);
    }
    
    @Quando("filtro pagamentos pelo status_pagamento {string}")
    public void filtrarPorStatus(String status) {
        response = RestAssured.given()
            .queryParam("status_pagamento", status)
            .get("/pagamentos");
    }
    
    @Então("todos os pagamentos devem ter status_pagamento {string}")
    public void verificarStatus(String status) {
        response.then().body("status_pagamento", everyItem(equalTo(status)));
    }
    
    @Quando("filtro pagamentos pelo metodo_pagamento {string}")
    public void filtrarPorMetodo(String metodo) {
        response = RestAssured.given()
            .queryParam("metodo_pagamento", metodo)
            .get("/pagamentos");
    }
    
    @Então("todos os pagamentos devem ter metodo_pagamento {string}")
    public void verificarMetodo(String metodo) {
        response.then().body("metodo_pagamento", everyItem(equalTo(metodo)));
    }
    
    @Dado("que existe um pagamento com pagamento_id {string}")
    public void buscarPagamentoPorId(String pagamentoId) {
        response = RestAssured.given()
            .queryParam("pagamento_id", pagamentoId)
            .get("/pagamentos");
        
        if (response.jsonPath().getList("$").size() > 0) {
            this.pagamentoId = response.jsonPath().getString("[0].id");
        }
    }
    
    @Quando("busco o pagamento pelo id")
    public void buscarPagamento() {
        response = RestAssured.get("/pagamentos/" + pagamentoId);
    }
    
    @Então("o pagamento deve estar vinculado ao pedido_id {string}")
    public void verificarPedidoId(String pedidoId) {
        assertEquals(pedidoId, response.jsonPath().getString("pedido_id"));
    }
    
    @Então("o pagamento deve ter valor_pago_brl {double}")
    public void verificarValorPago(double valor) {
        assertEquals(valor, response.jsonPath().getDouble("valor_pago_brl"), 0.01);
    }
}
