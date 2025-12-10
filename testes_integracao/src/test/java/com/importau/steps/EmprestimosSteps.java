package com.importau.steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

public class EmprestimosSteps {
    
    private String baseUrl;
    private Response response;
    private Map<String, Object> emprestimoData = new HashMap<>();
    private String emprestimoId;
    
    @Dado("que o microsserviço de empréstimos está disponível em {string}")
    public void configurarBaseUrl(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;
    }
    
    @Quando("solicito a lista de empréstimos")
    public void solicitarListaEmprestimos() {
        response = RestAssured.get("/emprestimos");
    }
    
    @Então("devo receber uma lista com pelo menos {int} empréstimo")
    public void verificarListaNaoVazia(int minimo) {
        assertTrue(response.jsonPath().getList("$").size() >= minimo);
    }
    
    @Dado("que existem empréstimos cadastrados")
    public void verificarEmprestimosExistem() {
        response = RestAssured.get("/emprestimos");
        assertTrue(response.jsonPath().getList("$").size() > 0);
    }
    
    @Quando("filtro empréstimos pelo status {string}")
    public void filtrarPorStatus(String status) {
        response = RestAssured.given()
            .queryParam("status", status)
            .get("/emprestimos");
    }
    
    @Então("todos os empréstimos devem ter status {string}")
    public void verificarStatus(String status) {
        response.then().body("status", everyItem(equalTo(status)));
    }
    
    @Quando("filtro empréstimos pela finalidade {string}")
    public void filtrarPorFinalidade(String finalidade) {
        response = RestAssured.given()
            .queryParam("finalidade", finalidade)
            .get("/emprestimos");
    }
    
    @Então("todos os empréstimos devem ter finalidade {string}")
    public void verificarFinalidade(String finalidade) {
        response.then().body("finalidade", everyItem(equalTo(finalidade)));
    }
    
    @Dado("que existe um empréstimo com emprestimo_id {string}")
    public void buscarEmprestimoPorId(String emprestimoId) {
        response = RestAssured.given()
            .queryParam("emprestimo_id", emprestimoId)
            .get("/emprestimos");
        
        if (response.jsonPath().getList("$").size() > 0) {
            this.emprestimoId = response.jsonPath().getString("[0].id");
        }
    }
    
    @Quando("busco o empréstimo pelo id")
    public void buscarEmprestimo() {
        response = RestAssured.get("/emprestimos/" + emprestimoId);
    }
    
    @Então("o empréstimo deve ter instituicao_financeira {string}")
    public void verificarInstituicao(String instituicao) {
        assertEquals(instituicao, response.jsonPath().getString("instituicao_financeira"));
    }
    
    @Então("o empréstimo deve ter valor_principal_brl {double}")
    public void verificarValorPrincipal(double valor) {
        assertEquals(valor, response.jsonPath().getDouble("valor_principal_brl"), 0.01);
    }
    
    @Então("o empréstimo deve ter taxa_juros_anual {double}")
    public void verificarTaxaJuros(double taxa) {
        assertEquals(taxa, response.jsonPath().getDouble("taxa_juros_anual"), 0.01);
    }
}
