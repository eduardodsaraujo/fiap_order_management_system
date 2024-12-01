package br.com.fiap.order_management.infra.gateway.integration;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
public class StockProductIntegrationGatewayIT {

    @Autowired
    private StockProductIntegrationGateway stockProductIntegrationGateway;

    @Test
    public void shouldIncreaseStock() {
        // Act
        stubFor(put("/product-management/api/products/stock/increase").willReturn(ok()));

        // Arrange
        stockProductIntegrationGateway.increaseStock(1L, 10);

        // Assert
        assertTrue(true);
    }

    @Test
    public void shouldDecreaseStock() {
        // Act
        stubFor(put("/product-management/api/products/stock/decrease")
                .willReturn(ok()));

        // Arrange
        stockProductIntegrationGateway.decreaseStock(1L, 10);

        // Assert
        assertTrue(true);
    }

}