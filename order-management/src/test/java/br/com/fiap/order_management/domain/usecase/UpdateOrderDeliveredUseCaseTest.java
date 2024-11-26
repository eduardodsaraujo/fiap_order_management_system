package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.UUID;
import static org.mockito.Mockito.*;

public class UpdateOrderDeliveredUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private UpdateOrderDeliveredUseCase updateOrderDeliveredUseCase;

    private UUID orderId;
    private Order order;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = mock(Order.class);
        when(orderGateway.findById(orderId)).thenReturn(order);
    }

    @Test
    void execute_ShouldUpdateOrderStatusToDelivered() {
        updateOrderDeliveredUseCase.execute(orderId);
        verify(order, times(1)).updateDelivered();
        verify(orderGateway, times(1)).save(order);
    }
}
