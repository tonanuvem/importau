package com.importau.pagamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicação principal do microsserviço de Pagamentos
 * Responsável por gerenciar transações financeiras e correlações com pedidos
 */
@SpringBootApplication
public class PagamentosApplication {

    public static void main(String[] args) {
        SpringApplication.run(PagamentosApplication.class, args);
    }
}
