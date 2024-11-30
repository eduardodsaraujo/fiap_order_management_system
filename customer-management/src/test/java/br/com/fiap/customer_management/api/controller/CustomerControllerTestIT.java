package br.com.fiap.customer_management.api.controller;

import br.com.fiap.customer_management.infrastructure.exception.MessageError;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static br.com.fiap.customer_management.utils.CustomerHelper.createCustomerRequestDTO;
import static io.restassured.RestAssured.given;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CustomerControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void shouldCreateCustomer() {
        var customerRequest = createCustomerRequestDTO();

        given()
               // .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerRequest)
                .when()
                .post("/api/customers")
                .then()
                .statusCode(HttpStatus.OK.value()) // O controlador retorna 200 em vez de 201
                .body(matchesJsonSchemaInClasspath("./schemas/CustomerResponseSchema.json"))
                .body("name", equalTo(customerRequest.getName()))
                .body("email", equalTo(customerRequest.getEmail()));
    }

    @Test
    void shouldDeleteCustomerById() {
        var id = 1L;

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/api/customers/{id}", id)
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void shouldFindCustomerById() {
        var id = 1;

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/customers/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/CustomerResponseSchema.json"))
                .body("id", equalTo(id)); // Certifique-se de converter para o tipo correto
    }

        @Test
    void shouldFindAllCustomers() {
        given()
               // .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/customers")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/FindAllCustomersResponseSchema.json"));
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldUpdateCustomer() {
        var id = 1;
        var customerUpdateRequest = createCustomerRequestDTO();
        customerUpdateRequest.setName("Henrique");
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(customerUpdateRequest)
                .when()
                .put("/api/customers/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/CustomerResponseSchema.json"))
                .body("name", equalTo(customerUpdateRequest.getName()))
                .body("email", equalTo(customerUpdateRequest.getEmail()));
    }
}
