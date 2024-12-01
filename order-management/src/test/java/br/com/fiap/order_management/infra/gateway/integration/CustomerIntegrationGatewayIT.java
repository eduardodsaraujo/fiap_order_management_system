package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.model.Customer;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.CustomerDto;
import br.com.fiap.order_management.util.CustomerHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static br.com.fiap.order_management.api.controller.OrderControllerTest.asJsonString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
public class CustomerIntegrationGatewayIT {

    @Autowired
    private CustomerIntegrationGateway customerIntegrationGateway;

    @Test
    public void shouldFindCustomerById() {
        // Act
        CustomerDto customerDto = CustomerHelper.createCustomerDto();

        stubFor(get("/customer-management/api/customers/1")
                .willReturn(okJson(asJsonString(customerDto))));


        // Arrange
        Customer customer = customerIntegrationGateway.findById(1L);

        // Assert
        assertThat(customer.getId()).isEqualTo(customerDto.getId());
        assertThat(customer.getName()).isEqualTo(customerDto.getName());
        assertThat(customer.getEmail()).isEqualTo(customerDto.getEmail());
        assertThat(customer.getPhone()).isEqualTo(customerDto.getPhone());
    }

}