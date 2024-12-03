package br.com.fiap.product_import.api.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductImportControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    public void shouldImportProducts() throws Exception {
        // Arrange
        String file = "IP1P;iphone 1 pro;iphone 16 pro 256;Smartphone;Apple;true;9000;2;20";

        // Act
        // Assert
        given()
                .when()
                .multiPart("file", file)
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldThrowException_WhenSaveFile_WithIncorrectFile() throws Exception {
        // Arrange
        String file = "";

        // Act
        // Assert
        given()
                .when()
                .multiPart("file", file)
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
