package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.PaymentGateway;
import br.com.fiap.order_management.domain.gateway.StockProductGateway;
import br.com.fiap.order_management.domain.input.PaymentInput;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
import br.com.fiap.order_management.domain.model.PaymentMethod;
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
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class OrderCheckoutUseCaseTest {


    private OrderCheckoutUseCase orderCheckoutUseCase;

    @Mock
    private OrderGateway orderGateway;
    @Mock
    private PaymentGateway paymentGateway;
    @Mock
    private StockProductGateway stockProductGateway;
    AutoCloseable openMocks;


    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        orderCheckoutUseCase = new OrderCheckoutUseCase(orderGateway, paymentGateway, stockProductGateway);
    }

    @Test
    public void shouldCheckoutOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());

        PaymentInput input = new PaymentInput(PaymentMethod.PIX);

        doNothing().when(stockProductGateway).decreaseStock(anyLong(), anyDouble());
        when(orderGateway.findById(any(UUID.class))).thenReturn(order);
        when(orderGateway.save(any(Order.class))).thenReturn(order);

        // Act
        OrderOutput updatedOrder = orderCheckoutUseCase.execute(orderId, input);

        // Assert
        verify(stockProductGateway, times(1)).decreaseStock(anyLong(), anyDouble());
        verify(orderGateway, times(1)).findById(any(UUID.class));
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(updatedOrder.getId()).isEqualTo(order.getId());
        assertThat(OrderStatus.WAITING_PAYMENT).isEqualTo(order.getStatus());
        assertThat(updatedOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(updatedOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(updatedOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(updatedOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(updatedOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(updatedOrder.getDeliveryAddress().getId()).isEqualTo(order.getDeliveryAddress().getId());
        assertThat(updatedOrder.getPayment().getId()).isEqualTo(order.getPayment().getId());

    }

}