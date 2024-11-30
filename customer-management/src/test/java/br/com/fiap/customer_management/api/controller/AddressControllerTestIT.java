package br.com.fiap.customer_management.api.controller;

import br.com.fiap.customer_management.application.AddressDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static br.com.fiap.customer_management.utils.AddressHelper.createAddressRequestDTO;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AddressControllerIT {

    @LocalServerPort
    private int port;

    private static final String BASE_URL = "/api/customers/{customerId}/addresses";

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    private AddressDTO createAddressForCustomer(Long customerId) {
        var addressRequest = createAddressRequestDTO();
        return given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addressRequest)
                .when()
                .post(BASE_URL, customerId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(AddressDTO.class);
    }

    @Test
    void shouldCreateAddress() {
        var customerId = 1L;
        var addressRequest = createAddressRequestDTO();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(addressRequest)
                .when()
                .post(BASE_URL, customerId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/AddressResponseSchema.json"))
                .body("street", equalTo(addressRequest.getStreet()))
                .body("city", equalTo(addressRequest.getCity()))
                .body("postalCode", equalTo(addressRequest.getPostalCode()));
    }

    @Test
    void shouldDeleteAddressById() {
        var customerId = 1L;
        var createdAddress = createAddressForCustomer(customerId);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(BASE_URL + "/{addressId}", customerId, createdAddress.getId())
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldFindAddressById() {
        var customerId = 1L;
        var createdAddress = createAddressForCustomer(customerId);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL + "/{addressId}", customerId, createdAddress.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .body(matchesJsonSchemaInClasspath("./schemas/AddressResponseSchema.json"))
                .body("id", equalTo(createdAddress.getId().intValue()));
    }

    @Test
    void shouldFindAllAddresses() {
        var customerId = 1L;
        var createdAddress = createAddressForCustomer(customerId);
        var createdAddress2 = createAddressForCustomer(customerId);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL, customerId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/FindAllAddressesResponseSchema.json"));
    }

    @Test
    void shouldUpdateAddress() {
        var customerId = 1L;
        var createdAddress = createAddressForCustomer(customerId);
        var updatedAddressRequest = createAddressRequestDTO();
        updatedAddressRequest.setStreet("Updated Street");


        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(updatedAddressRequest)
                .when()
                .put(BASE_URL + "/{addressId}", customerId, createdAddress.getId())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/AddressResponseSchema.json"))
                .body("street", equalTo(updatedAddressRequest.getStreet()))
                .body("city", equalTo(updatedAddressRequest.getCity()))
                .body("postalCode", equalTo(updatedAddressRequest.getPostalCode()));
    }
}
