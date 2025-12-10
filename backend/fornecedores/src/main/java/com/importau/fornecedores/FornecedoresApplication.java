package com.importau.fornecedores;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação principal do microsserviço de Fornecedores
 * Responsável por gerenciar dados de fornecedores e suas informações
 */
@SpringBootApplication
public class FornecedoresApplication {

    public static void main(String[] args) {
        SpringApplication.run(FornecedoresApplication.class, args);
    }
}
