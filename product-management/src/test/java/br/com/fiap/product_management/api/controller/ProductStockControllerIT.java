package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.application.input.UpdateStockInput;
import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductStockControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        JsonConfig jsonConfig = JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE);
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig);
    }

    @Test
    public void shouldIncreaseProductStock() throws Exception {
        // Arrange
        long productId = 1L;
        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateStockInput)
                .when()
                .put("/api/products/stock/increase")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldDecreaseProductStock() throws Exception {
        // Arrange
        long productId = 1L;
        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateStockInput)
                .when()
                .put("/api/products/stock/decrease")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    public void shouldThrowException_WhenDecreaseProductStock_WithInsufficientStockQuantity() throws Exception {
        // Arrange
        long productId = 1L;
        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 1100);

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateStockInput)
                .when()
                .put("/api/products/stock/decrease")
                .then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body("message", equalTo("Insufficient product stock"));
    }
}