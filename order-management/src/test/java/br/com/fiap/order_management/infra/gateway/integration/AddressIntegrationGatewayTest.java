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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddressIntegrationGatewayTest {

    private AddressIntegrationGateway addressIntegrationGateway;

    @Mock
    private AddressWsGateway addressWsGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        addressIntegrationGateway = new AddressIntegrationGateway(addressWsGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    public void shouldFindAddressByCustomerIdAndAddressId() {
        // Act
        AddressDto addressDto = CustomerHelper.createAddressDto();

        when(addressWsGateway.findByCustomerIdAndAddressId(any(Long[].class))).thenReturn(addressDto);

        // Arrange
        DeliveryAddress deliveryAddress = addressIntegrationGateway.findAddressByCustomerIdAndAddressId(1L, 2L);

        // Assert
        verify(addressWsGateway, times(1)).findByCustomerIdAndAddressId(any(Long[].class));
        assertThat(deliveryAddress.getId()).isEqualTo(addressDto.getId());
        assertThat(deliveryAddress.getStreet()).isEqualTo(addressDto.getStreet());
        assertThat(deliveryAddress.getNumber()).isEqualTo(addressDto.getNumber());
        assertThat(deliveryAddress.getComplement()).isEqualTo(addressDto.getComplement());
        assertThat(deliveryAddress.getDistrict()).isEqualTo(addressDto.getDistrict());
        assertThat(deliveryAddress.getCity()).isEqualTo(addressDto.getCity());
        assertThat(deliveryAddress.getState()).isEqualTo(addressDto.getState());
        assertThat(deliveryAddress.getPostalCode()).isEqualTo(addressDto.getPostalCode());
    }

}