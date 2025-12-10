package com.importau.steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.junit.Assert.*;

public class CommonSteps {
    
    private static Response response;
    
    public static void setResponse(Response r) {
        response = r;
    }
    
    public static Response getResponse() {
        return response;
    }
    
    @Então("devo receber status {int}")
    public void verificarStatus(int statusCode) {
        if (response != null) {
            assertEquals(statusCode, response.getStatusCode());
        }
    }
    
    @Quando("acesso o endpoint {string}")
    public void acessarEndpoint(String endpoint) {
        response = RestAssured.get(endpoint);
    }
    
    @Então("a resposta deve conter {string} igual a {string}")
    public void verificarCampoResposta(String campo, String valor) {
        if (response != null) {
            assertEquals(valor, response.jsonPath().getString(campo));
        }
    }
    
    @Então("o {word} deve ter fornecedor_id {string}")
    public void verificarFornecedorId(String tipo, String fornecedorId) {
        if (response != null) {
            assertEquals(fornecedorId, response.jsonPath().getString("fornecedor_id"));
        }
    }
}
