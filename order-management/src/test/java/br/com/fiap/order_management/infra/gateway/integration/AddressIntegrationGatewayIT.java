package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;
import br.com.fiap.order_management.util.CustomerHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static br.com.fiap.order_management.util.JsonHelper.asJsonString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
public class AddressIntegrationGatewayIT {

    @Autowired
    private AddressIntegrationGateway addressIntegrationGateway;

    @Test
    public void shouldFindAddressByCustomerIdAndAddressId() {
        // Act
        AddressDto addressDto = CustomerHelper.createAddressDto();

        stubFor(get("/customer-management/api/customers/1/addresses/1")
                .willReturn(okJson(asJsonString(addressDto))));

        // Arrange
        DeliveryAddress deliveryAddress = addressIntegrationGateway.findAddressByCustomerIdAndAddressId(1L, 1L);

        // Assert
        assertThat(deliveryAddress).isNotNull();
    }

}