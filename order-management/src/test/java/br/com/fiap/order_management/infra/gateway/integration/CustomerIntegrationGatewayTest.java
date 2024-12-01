package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.CustomerDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.CustomerWsGateway;
import br.com.fiap.order_management.util.CustomerHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CustomerIntegrationGatewayTest {

    private CustomerIntegrationGateway customerIntegrationGateway;

    @Mock
    private CustomerWsGateway customerWsGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        customerIntegrationGateway = new CustomerIntegrationGateway(customerWsGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    public void shouldFindCustomerById() {
        // Act
        CustomerDto customerDto = CustomerHelper.createCustomerDto();

        when(customerWsGateway.findById(anyLong())).thenReturn(customerDto);

        // Arrange
        Customer customer = customerIntegrationGateway.findById(1L);

        // Assert
        verify(customerWsGateway, times(1)).findById(anyLong());
        assertThat(customer.getId()).isEqualTo(customerDto.getId());
        assertThat(customer.getName()).isEqualTo(customerDto.getName());
        assertThat(customer.getEmail()).isEqualTo(customerDto.getEmail());
        assertThat(customer.getPhone()).isEqualTo(customerDto.getPhone());
    }

}