package br.com.fiap.delivery_logistics.controller;

import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryTrackRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DeliveryControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void shouldCreateDelivery() {
        DeliveryRequestDto deliveryRequest = DeliveryRequestDto.builder()
                .orderId(UUID.randomUUID())
                .destinationZipCode("12345678")
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(deliveryRequest)
                .when()
                .post("/api/delivery")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("status", equalTo(DeliveryStatus.PENDING.toString()))
                .body("orderId", equalTo(deliveryRequest.getOrderId().toString()))
                .body("destinationZipCode", equalTo(deliveryRequest.getDestinationZipCode()));
    }

    @Test
    void shouldUpdateDeliveryStatus() {
        UUID deliveryId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        ChangeDeliveryStatusRequestDto statusRequest = ChangeDeliveryStatusRequestDto.builder()
                .deliveryId(deliveryId)
                .status(DeliveryStatus.DELIVERED)
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(statusRequest)
                .when()
                .put("/api/delivery/{id}", deliveryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("DELIVERED"));  // Ajuste conforme o status esperado
    }

    @Test
    void shouldGetDeliveryById() {
        UUID deliveryId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/delivery/{id}", deliveryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("orderId", equalTo(deliveryId.toString()));
    }

    @Test
    void shouldCalculateShipping() {
        CalculateShippingRequestDto requestDto = CalculateShippingRequestDto.builder()
                .destinationZipCode("12345678")
                .weightProducts(500)
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when()
                .post("/api/delivery/calculate-shipping")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("shippingCost", equalTo(100.0))  // Ajuste conforme o valor esperado
                .body("estimatedDeliveryTime", equalTo("3 days"));  // Ajuste conforme o valor esperado
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void shouldUpdateDeliveryTrack() {
        UUID deliveryId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
        DeliveryTrackRequestDto trackRequest = DeliveryTrackRequestDto.builder()
                .orderId(deliveryId)
                .latitude(new BigDecimal("40.7128"))
                .longitude(new BigDecimal("-74.0060"))
                .build();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(trackRequest)
                .when()
                .put("/api/delivery/update-track")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("latitude", equalTo(trackRequest.getLatitude()))
                .body("longitude", equalTo(trackRequest.getLongitude()));
    }
}
