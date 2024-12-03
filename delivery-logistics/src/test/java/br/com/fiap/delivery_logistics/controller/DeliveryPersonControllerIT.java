package br.com.fiap.delivery_logistics.controller;

import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.VehicleType;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestExecution;

import java.sql.SQLException;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class DeliveryPersonControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void shouldCreateDeliveryPerson() {
        var deliveryPersonRequest = new DeliveryPersonRequestDto("John Doe", VehicleType.BICYCLE);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deliveryPersonRequest)
                .when()
                .post("/api/deliveryperson")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("./schemas/DeliveryPersonResponseSchema.json"))
                .body("name", equalTo(deliveryPersonRequest.getName()))
                .body("vehicleType", equalTo(deliveryPersonRequest.getVehicleType().toString()))
                .body("status", equalTo(DeliveryPersonStatus.AVAILABLE.toString()));
    }

    @Test
    void shouldUpdateDeliveryPersonStatus() {
        var deliveryId = 1L;
        var statusUpdateRequest = new ChangeDeliveryPersonStatusRequestDto(deliveryId, DeliveryPersonStatus.UNAVAILABLE);

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(statusUpdateRequest)
                .when()
                .put("/api/deliveryperson/{id}", deliveryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/DeliveryPersonResponseSchema.json"))
                .body("status", equalTo(statusUpdateRequest.getStatus().toString()));
    }

    @Test
    void shouldFindAllDeliveryPeople() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/deliveryperson")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/FindAllDeliveryPeopleResponseSchema.json"));
    }

    @Test
    void shouldAssignAvailableDeliveryPeople() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/deliveryperson/assign-available-delivery-people")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void shouldCalculateRoutesByRegion() {
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/deliveryperson/calculate-routes")
                .then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .body(matchesJsonSchemaInClasspath("./schemas/CalculateRoutesResponseSchema.json"));
    }
}
