package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.api.controller.OrderControllerTest;
import br.com.fiap.order_management.domain.gateway.AddressGateway;
import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingDto;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.OrderHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static br.com.fiap.order_management.util.JsonHelper.asJsonString;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@WireMockTest(httpPort = 8080)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class UpdateOrderDeliveryAddressUseCaseIT {

    @Autowired
    private UpdateOrderDeliveryAddressUseCase updateOrderDeliveryAddressUseCase;
    @Autowired
    private OrderGateway orderGateway;

    @Test
    public void shouldUpdateOrderDeliveryAddress() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        orderGateway.save(order);

        UpdateDeliveryAddressInput input = new UpdateDeliveryAddressInput(1L);

        AddressDto addressDto = CustomerHelper.createAddressDto();
        stubFor(get("/customer-management/api/customers/1/addresses/1")
                .willReturn(okJson(asJsonString(addressDto))));

        CalculateShippingDto calculateShippingDto = new CalculateShippingDto(10.0, 5);
        stubFor(post("/delivery-logistics/api/delivery/calculate-shipping")
                .willReturn(okJson(OrderControllerTest.asJsonString(calculateShippingDto))));

        // Act
        OrderOutput updatedOrder = updateOrderDeliveryAddressUseCase.execute(orderId, input);

        // Assert
        assertThat(updatedOrder.getId()).isEqualTo(order.getId());
        assertThat(updatedOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(updatedOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(updatedOrder.getShippingValue()).isEqualTo(10.0);
        assertThat(updatedOrder.getTotal()).isEqualTo(16010.0);
        assertThat(updatedOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(updatedOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(updatedOrder.getDeliveryAddress().getId()).isNotNull();
        assertThat(updatedOrder.getPayment()).isEqualTo(order.getPayment());
        assertThat(updatedOrder.getItems()).hasSize(1);
    }

}
