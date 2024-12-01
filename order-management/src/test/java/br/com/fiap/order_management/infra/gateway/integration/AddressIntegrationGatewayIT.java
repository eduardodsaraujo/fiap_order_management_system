package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.AddressWsGateway;
import br.com.fiap.order_management.util.CustomerHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class AddressIntegrationGatewayIT {

    @Autowired
    private AddressIntegrationGateway addressIntegrationGateway;

    @Test
    public void shouldFindAddressByCustomerIdAndAddressId() {
        // Act
        // Arrange
        DeliveryAddress deliveryAddress = addressIntegrationGateway.findAddressByCustomerIdAndAddressId(1L, 1L);

        // Assert
        assertThat(deliveryAddress).isNotNull();
    }

}