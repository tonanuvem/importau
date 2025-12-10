package com.importau.steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class FornecedoresSteps {
    
    private String baseUrl;
    private Response response;
    private String fornecedorId;
    
    @Dado("que o microsserviço de fornecedores está disponível em {string}")
    public void configurarBaseUrl(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;
    }
    
    @Quando("solicito a lista de fornecedores")
    public void solicitarListaFornecedores() {
        response = RestAssured.get("/fornecedores");
    }
    
    @Então("devo receber uma lista com pelo menos {int} fornecedor")
    public void verificarListaNaoVazia(int minimo) {
        assertTrue(response.jsonPath().getList("$").size() >= minimo);
    }
    
    @Dado("que existem fornecedores cadastrados")
    public void verificarFornecedoresExistem() {
        response = RestAssured.get("/fornecedores");
        assertTrue(response.jsonPath().getList("$").size() > 0);
    }
    
    @Quando("filtro fornecedores pela categoria {string}")
    public void filtrarPorCategoria(String categoria) {
        response = RestAssured.given()
            .queryParam("categoria", categoria)
            .get("/fornecedores");
    }
    
    @Então("todos os fornecedores devem ter categoria {string}")
    public void verificarCategoria(String categoria) {
        response.then().body("categoria", everyItem(equalTo(categoria)));
    }
    
    @Quando("filtro fornecedores pelo rating {string}")
    public void filtrarPorRating(String rating) {
        response = RestAssured.given()
            .queryParam("rating", rating)
            .get("/fornecedores");
    }
    
    @Então("todos os fornecedores devem ter rating {string}")
    public void verificarRating(String rating) {
        response.then().body("rating", everyItem(equalTo(rating)));
    }
    
    @Dado("que existe um fornecedor com fornecedor_id {string}")
    public void buscarFornecedorPorId(String fornecedorId) {
        response = RestAssured.given()
            .queryParam("fornecedor_id", fornecedorId)
            .get("/fornecedores");
        
        if (response.jsonPath().getList("$").size() > 0) {
            this.fornecedorId = response.jsonPath().getString("[0].id");
        }
    }
    
    @Quando("busco o fornecedor pelo id")
    public void buscarFornecedor() {
        response = RestAssured.get("/fornecedores/" + fornecedorId);
    }
    
    @Então("o fornecedor deve ter razao_social {string}")
    public void verificarRazaoSocial(String razaoSocial) {
        assertEquals(razaoSocial, response.jsonPath().getString("razao_social"));
    }
    
    @Então("o fornecedor deve ter cnpj {string}")
    public void verificarCnpj(String cnpj) {
        assertEquals(cnpj, response.jsonPath().getString("cnpj"));
    }
    
    @Então("o fornecedor deve ter pais_origem {string}")
    public void verificarPaisOrigem(String pais) {
        assertEquals(pais, response.jsonPath().getString("pais_origem"));
    }
}
