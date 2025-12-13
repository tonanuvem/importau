package com.importau.pagamentos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do OpenAPI/Swagger para documentação da API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pagamentos API - IMPORTAÚ")
                        .description("API para gerenciamento de pagamentos do sistema IMPORTAÚ")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe IMPORTAÚ")
                                .email("dev@importau.com")));
    }
}
