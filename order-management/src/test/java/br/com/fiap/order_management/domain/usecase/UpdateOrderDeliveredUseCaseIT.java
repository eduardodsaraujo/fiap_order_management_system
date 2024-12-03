package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UpdateOrderDeliveredUseCaseIT {

    private UpdateOrderDeliveredUseCase updateOrderDeliveredUseCase;

    @Mock
    private OrderGateway orderGateway;
    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        updateOrderDeliveredUseCase = new UpdateOrderDeliveredUseCase(orderGateway);
    }

    @Test
    void shouldUpdateOrderDelivered() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);

        when(orderGateway.findById(any(UUID.class))).thenReturn(order);
        when(orderGateway.save(any(Order.class))).thenReturn(order);

        // Act
        updateOrderDeliveredUseCase.execute(orderId);

        // Assert
        verify(orderGateway, times(1)).findById(any(UUID.class));
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(OrderStatus.DELIVERED).isEqualTo(order.getStatus());
    }

}
