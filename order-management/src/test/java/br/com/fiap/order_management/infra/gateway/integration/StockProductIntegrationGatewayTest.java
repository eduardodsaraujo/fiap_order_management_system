package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductStockRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.ProductWsGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StockProductIntegrationGatewayTest {

    private StockProductIntegrationGateway stockProductIntegrationGateway;

    @Mock
    private ProductWsGateway productWsGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        stockProductIntegrationGateway = new StockProductIntegrationGateway(productWsGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    public void shouldIncreaseStock() {
        // Act
        doNothing().when(productWsGateway).increaseStock(any(ProductStockRequestDto.class));

        // Arrange
        stockProductIntegrationGateway.increaseStock(1L, 10);

        // Assert
        verify(productWsGateway, times(1)).increaseStock(any(ProductStockRequestDto.class));
    }

    @Test
    public void shouldDecreaseStock() {
        // Act
        doNothing().when(productWsGateway).decreaseStock(any(ProductStockRequestDto.class));

        // Arrange
        stockProductIntegrationGateway.decreaseStock(1L, 10);

        // Assert
        verify(productWsGateway, times(1)).decreaseStock(any(ProductStockRequestDto.class));
    }

}