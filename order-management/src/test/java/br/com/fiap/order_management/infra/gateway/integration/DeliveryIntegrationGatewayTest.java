package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.DeliveryRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.DeliveryWsGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DeliveryIntegrationGatewayTest {

    private DeliveryIntegrationGateway deliveryIntegrationGateway;

    @Mock
    private DeliveryWsGateway deliveryWsGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        deliveryIntegrationGateway = new DeliveryIntegrationGateway(deliveryWsGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    public void shouldCalculateShipping() {
        // Act
        CalculateShippingDto calculateShippingDto = new CalculateShippingDto(10.0, 5);

        when(deliveryWsGateway.calculateShipping(any(CalculateShippingRequestDto.class))).thenReturn(calculateShippingDto);

        // Arrange
        double valueShipping = deliveryIntegrationGateway.calculateShipping("12345000", 1.0);

        // Assert
        verify(deliveryWsGateway, times(1)).calculateShipping(any(CalculateShippingRequestDto.class));
        assertThat(valueShipping).isEqualTo(calculateShippingDto.getCost());
    }

    @Test
    public void shouldCreateDelivery() {
        // Act
        UUID orderId = UUID.randomUUID();

        doNothing().when(deliveryWsGateway).createDelivery(any(DeliveryRequestDto.class));

        // Arrange
        deliveryIntegrationGateway.createDelivery(orderId, "12345000");

        // Assert
        verify(deliveryWsGateway, times(1)).createDelivery(any(DeliveryRequestDto.class));
    }

}