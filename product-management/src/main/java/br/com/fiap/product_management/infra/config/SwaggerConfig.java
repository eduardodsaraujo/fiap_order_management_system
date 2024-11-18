package br.com.fiap.product_management.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi() {

        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API Ptoduct Management - FIAP")
                        .description("Spring Boot API for product management.")
                        .version("v1")
                        .contact(new Contact()
                                .name("FIAP - Pós Tech")
                                .url("https://on.fiap.com.br/")));

    }
}
