package com.importau.steps;

import io.cucumber.java.pt.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Map;
import java.util.HashMap;

public class ProdutosSteps {
    
    private String baseUrl;
    private Response response;
    private Map<String, Object> produtoData = new HashMap<>();
    private String produtoId;
    
    @Dado("que o microsserviço de produtos está disponível em {string}")
    public void configurarBaseUrl(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;
    }
    
    @Quando("solicito a lista de produtos")
    public void solicitarListaProdutos() {
        response = RestAssured.get("/produtos");
        CommonSteps.setResponse(response);
    }
    
    @Então("devo receber uma lista com pelo menos {int} produto")
    public void verificarListaNaoVazia(int minimo) {
        assertTrue(response.jsonPath().getList("$").size() >= minimo);
    }
    
    @Dado("que existem produtos cadastrados")
    public void verificarProdutosExistem() {
        response = RestAssured.get("/produtos");
        assertTrue(response.jsonPath().getList("$").size() > 0);
    }
    
    @Quando("busco produtos da categoria {string}")
    public void buscarPorCategoria(String categoria) {
        response = RestAssured.given()
            .queryParam("categoria", categoria)
            .get("/produtos");
        CommonSteps.setResponse(response);
    }
    
    @Então("todos os produtos devem ser da categoria {string}")
    public void verificarCategoria(String categoria) {
        response.then().body("categoria", everyItem(equalTo(categoria)));
    }
    
    @Dado("que tenho os dados de um novo produto:")
    public void prepararDadosProduto(Map<String, String> dados) {
        produtoData.clear();
        produtoData.put("codigo", dados.get("codigo"));
        produtoData.put("nome", dados.get("nome"));
        produtoData.put("categoria", dados.get("categoria"));
        produtoData.put("preco_unitario_brl", Double.parseDouble(dados.get("preco_unitario_brl")));
        produtoData.put("fornecedor_id", dados.get("fornecedor_id"));
        produtoData.put("estoque_atual", Integer.parseInt(dados.get("estoque_atual")));
        produtoData.put("estoque_minimo", 10);
        produtoData.put("ativo", true);
    }
    
    @Quando("crio o produto")
    public void criarProduto() {
        response = RestAssured.given()
            .contentType("application/json")
            .body(produtoData)
            .post("/produtos");
        CommonSteps.setResponse(response);
        
        if (response.getStatusCode() == 201) {
            produtoId = response.jsonPath().getString("id");
        }
    }
    
    @Então("o produto deve conter o codigo {string}")
    public void verificarCodigoProduto(String codigo) {
        assertEquals(codigo, response.jsonPath().getString("codigo"));
    }
    
    @Dado("que existe um produto com codigo {string}")
    public void buscarProdutoPorCodigo(String codigo) {
        response = RestAssured.given()
            .queryParam("codigo", codigo)
            .get("/produtos");
        
        if (response.jsonPath().getList("$").size() > 0) {
            produtoId = response.jsonPath().getString("[0].id");
        }
    }
    
    @Quando("busco o produto pelo codigo")
    public void buscarProduto() {
        response = RestAssured.get("/produtos/" + produtoId);
        CommonSteps.setResponse(response);
    }
    
    @Então("o produto deve ter o nome {string}")
    public void verificarNome(String nome) {
        assertEquals(nome, response.jsonPath().getString("nome"));
    }
    
    @Então("o produto deve ter a categoria {string}")
    public void verificarCategoriaUnica(String categoria) {
        assertEquals(categoria, response.jsonPath().getString("categoria"));
    }
    
    @Então("o produto deve ter preco_unitario_brl {int}")
    public void verificarPreco(int preco) {
        assertEquals(preco, response.jsonPath().getInt("preco_unitario_brl"));
    }
    
    @Quando("atualizo o estoque_atual para {int}")
    public void atualizarEstoque(int novoEstoque) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("estoque_atual", novoEstoque);
        
        response = RestAssured.given()
            .contentType("application/json")
            .body(updateData)
            .put("/produtos/" + produtoId);
        CommonSteps.setResponse(response);
    }
    
    @Então("o produto deve ter estoque_atual igual a {int}")
    public void verificarEstoque(int estoque) {
        assertEquals(estoque, response.jsonPath().getInt("estoque_atual"));
    }
    
    @Quando("solicito produtos com estoque abaixo do mínimo")
    public void buscarEstoqueBaixo() {
        response = RestAssured.get("/produtos?estoque_baixo=true");
        CommonSteps.setResponse(response);
    }
    
    @Então("todos os produtos devem ter estoque_atual menor que estoque_minimo")
    public void verificarEstoqueBaixo() {
        response.jsonPath().getList("$").forEach(item -> {
            Map<String, Object> produto = (Map<String, Object>) item;
            int atual = (int) produto.get("estoque_atual");
            int minimo = (int) produto.get("estoque_minimo");
            assertTrue(atual < minimo);
        });
    }
}
