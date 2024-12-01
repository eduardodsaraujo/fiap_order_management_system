package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingDto;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static br.com.fiap.order_management.api.controller.OrderControllerTest.asJsonString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
public class DeliveryIntegrationGatewayIT {

    @Autowired
    private DeliveryIntegrationGateway deliveryIntegrationGateway;

    @Test
    public void shouldCalculateShipping() {
        // Act
        CalculateShippingDto calculateShippingDto = new CalculateShippingDto(10.0, 5);

        stubFor(post("/delivery-logistics/api/delivery/calculate-shipping")
                .willReturn(okJson(asJsonString(calculateShippingDto))));

        // Arrange
        double valueShipping = deliveryIntegrationGateway.calculateShipping("12345000", 1.0);

        // Assert
        assertThat(valueShipping).isEqualTo(calculateShippingDto.getCost());
    }

    @Test
    public void shouldCreateDelivery() {
        // Act
        UUID orderId = UUID.randomUUID();

        stubFor(post("/delivery-logistics/api/delivery").willReturn(ok()));

        // Arrange
        deliveryIntegrationGateway.createDelivery(orderId, "12345000");

        // Assert
    }

}