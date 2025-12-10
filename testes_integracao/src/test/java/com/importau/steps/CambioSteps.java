package com.importau.steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.junit.Assert.*;

public class CambioSteps {
    
    private String baseUrl;
    private Response response;
    
    @Dado("que o microsserviço de câmbio está disponível em {string}")
    public void configurarBaseUrl(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;
    }
    
    @Quando("solicito a lista de operações de câmbio")
    public void solicitarListaCambio() {
        response = RestAssured.get("/cambio");
    }
    
    @Então("devo receber uma lista com pelo menos {int} operação")
    public void verificarListaNaoVazia(int minimo) {
        assertTrue(response.jsonPath().getList("$").size() >= minimo);
    }
    
    @Dado("que existem cotações cadastradas")
    public void verificarCotacoesExistem() {
        response = RestAssured.get("/cambio");
        assertTrue(response.jsonPath().getList("$").size() > 0);
    }
    
    @Quando("consulto a taxa de câmbio para {string}")
    public void consultarTaxaPorMoeda(String moeda) {
        response = RestAssured.given()
            .queryParam("moeda", moeda)
            .get("/cambio");
    }
    
    @Então("a moeda deve ser {string}")
    public void verificarMoeda(String moeda) {
        assertEquals(moeda, response.jsonPath().getString("[0].moeda"));
    }
    
    @Então("deve conter taxa_compra e taxa_venda")
    public void verificarTaxas() {
        assertNotNull(response.jsonPath().get("[0].taxa_compra"));
        assertNotNull(response.jsonPath().get("[0].taxa_venda"));
    }
}
