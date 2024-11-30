package br.com.fiap.customer_management.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    @Test
    void shouldReturnCustomOpenApiWithCorrectConfigurations() {
        // Arrange
        SwaggerConfig swaggerConfig = new SwaggerConfig();

        // Act
        OpenAPI openAPI = swaggerConfig.customOpenApi();

        // Assert
        assertNotNull(openAPI);
        assertNotNull(openAPI.getInfo());
        assertEquals("API Customer Management - FIAP", openAPI.getInfo().getTitle());
        assertEquals("Spring Boot API for customer management.", openAPI.getInfo().getDescription());
        assertEquals("v1", openAPI.getInfo().getVersion());

        Contact contact = openAPI.getInfo().getContact();
        assertNotNull(contact);
        assertEquals("FIAP - Pós Tech", contact.getName());
        assertEquals("https://on.fiap.com.br/", contact.getUrl());
    }
}
