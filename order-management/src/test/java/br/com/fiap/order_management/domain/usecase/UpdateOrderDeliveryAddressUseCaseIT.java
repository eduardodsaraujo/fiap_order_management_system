package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.AddressGateway;
import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.input.UpdateDeliveryAddressInput;
import br.com.fiap.order_management.domain.model.DeliveryAddress;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UpdateOrderDeliveryAddressUseCaseIT {

    private UpdateOrderDeliveryAddressUseCase updateOrderDeliveryAddressUseCase;

    @Mock
    private OrderGateway orderGateway;
    @Mock
    private AddressGateway addressGateway;
    @Mock
    private DeliveryGateway deliveryGateway;
    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        updateOrderDeliveryAddressUseCase = new UpdateOrderDeliveryAddressUseCase(orderGateway, addressGateway, deliveryGateway);
    }

    @Test
    void shouldUpdateOrderDeliveryAddress() {
        // Arrange
        DeliveryAddress deliveryAddress = CustomerHelper.createDeliveryAddress();

        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);

        UpdateDeliveryAddressInput input = new UpdateDeliveryAddressInput(1L);

        when(addressGateway.findAddressByCustomerIdAndAddressId(anyLong(), anyLong())).thenReturn(deliveryAddress);
        when(deliveryGateway.calculateShipping(anyString(), anyDouble())).thenReturn(10.0);
        when(orderGateway.findById(any(UUID.class))).thenReturn(order);
        when(orderGateway.save(any(Order.class))).thenReturn(order);

        // Act
        OrderOutput updatedOrder = updateOrderDeliveryAddressUseCase.execute(orderId, input);

        // Assert
        verify(addressGateway, times(1)).findAddressByCustomerIdAndAddressId(anyLong(), anyLong());
        verify(deliveryGateway, times(1)).calculateShipping(anyString(), anyDouble());
        verify(orderGateway, times(1)).findById(any(UUID.class));
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(updatedOrder.getId()).isEqualTo(order.getId());
        assertThat(updatedOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(updatedOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(updatedOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(updatedOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(updatedOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(updatedOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(updatedOrder.getDeliveryAddress().getId()).isEqualTo(order.getDeliveryAddress().getId());
        assertThat(updatedOrder.getPayment()).isEqualTo(order.getPayment());
        assertThat(updatedOrder.getItems()).hasSize(1);
    }

}
