package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class FindAllOrdersByCustomerIdUseCaseTest {

    private FindAllOrdersByCustomerIdUseCase findAllOrdersByCustomerIdUseCase;

    @Mock
    private OrderGateway orderGateway;
    AutoCloseable openMocks;


    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        findAllOrdersByCustomerIdUseCase = new FindAllOrdersByCustomerIdUseCase(orderGateway);
    }

    @Test
    void shouldFindAllOrdersByCustomerId() {
        // Arrange
        long customerId = 1L;

        Order order1 = OrderHelper.createOrder();
        Order order2 = OrderHelper.createOrder();
        var orders = Arrays.asList(order1, order2);

        when(orderGateway.findAllByCustomerId(anyLong())).thenReturn(orders);

        // Act
        var foundOrders = findAllOrdersByCustomerIdUseCase.execute(customerId);

        // Assert
        verify(orderGateway, times(1)).findAllByCustomerId(customerId);
        assertThat(foundOrders).hasSize(2);
    }
}
