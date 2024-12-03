package br.com.fiap.delivery_logistics.controller;

import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryTrackRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.infrastructure.client.OrderManagementClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.math.BigDecimal;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class DeliveryControllerIT {

    @LocalServerPort
    private int port;

    @MockBean
    private OrderManagementClient orderManagementClient; // Mockando o client

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

        when(orderManagementClient.updateDelivered(statusRequest.getDeliveryId())).thenReturn(ResponseEntity.ok().build());

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(statusRequest)
                .when()
                .put("/api/delivery/{id}", deliveryId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("status", equalTo("DELIVERED"));
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
                .body("cost", equalTo(1001.0F))
                .body("deliveryTime", equalTo(2));
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
                .body("latitude", equalTo(trackRequest.getLatitude().floatValue()))
                .body("longitude", equalTo(trackRequest.getLongitude().floatValue()));
    }
}

