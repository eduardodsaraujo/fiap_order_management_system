package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.utils.ProductHelper;
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

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerIT {

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
    public void shouldCreateProduct() {
        // Arrange
        CreateProductInput productInput = ProductHelper.createProductInput();

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productInput)
                .when()
                .post("/api/products")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ProductResponseSchema.json"))
                .body("code", equalTo(productInput.getCode()))
                .body("name", equalTo(productInput.getName()))
                .body("description", equalTo(productInput.getDescription()))
                .body("category", equalTo(productInput.getCategory()))
                .body("enable", equalTo(true))
                .body("price", equalTo(productInput.getPrice()))
                .body("weight", equalTo(productInput.getWeight()));

    }

    @Test
    public void shouldUpdateProduct() {
        // Arrange
        long productId = 1L;

        UpdateProductInput updateProductInput = UpdateProductInput.builder()
                .code("IP16PM")
                .name("IPhone 16 Pro Max")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .price(8000.0)
                .weight(1.0)
                .build();

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updateProductInput)
                .when()
                .put("/api/products/{productId}", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ProductResponseSchema.json"))
                .body("code", equalTo(updateProductInput.getCode()))
                .body("name", equalTo(updateProductInput.getName()))
                .body("description", equalTo(updateProductInput.getDescription()))
                .body("category", equalTo(updateProductInput.getCategory()))
                .body("price", equalTo(updateProductInput.getPrice()))
                .body("weight", equalTo(updateProductInput.getWeight()));
    }

    @Test
    public void shouldEnableProduct() {
        // Arrange
        long productId = 1L;

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/products/{productId}/enable", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ProductResponseSchema.json"))
                .body("enable", is(true));

    }

    @Test
    public void shouldDisableProduct() {
        // Arrange
        long productId = 1L;

        // Act
        // Assert
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/api/products/{productId}/disable", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ProductResponseSchema.json"))
                .body("enable", is(false));
    }

    @Test
    public void shouldFindProductById() {
        // Arrange
        long productId = 1L;

        // Act
        // Assert
        given()
                .when()
                .get("/api/products/{productId}", productId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ProductResponseSchema.json"));

    }

    @Test
    public void shouldFindAllProductsById() {
        // Arrange
        long productId = 1L;
        long productId2 = 2L;

        // Act
        // Assert
        given()
                .when()
                .get("/api/products/all/{productsId}", Arrays.asList(productId, productId2))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ProductArrayResponseSchema.json"));

    }

    @Test
    public void shouldFindAllProductsByName() {
        // Arrange
        // Act
        // Assert
        given()
                .when()
                .get("/api/products?name=%&page=0&size=10")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/ProductArrayResponseSchema.json"));

    }


}