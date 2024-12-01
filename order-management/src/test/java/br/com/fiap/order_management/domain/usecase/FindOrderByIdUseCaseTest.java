package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class FindOrderByIdUseCaseTest {

    private FindOrderByIdUseCase findOrderByIdUseCase;

    @Mock
    private OrderGateway orderGateway;
    AutoCloseable openMocks;


    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        findOrderByIdUseCase = new FindOrderByIdUseCase(orderGateway);
    }

    @Test
    void shouldFindOrderByIdUseCase() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);

        when(orderGateway.findById(any(UUID.class))).thenReturn(order);

        OrderOutput foundOrder = findOrderByIdUseCase.execute(orderId);

        assertThat(foundOrder).isInstanceOf(OrderOutput.class).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
        assertThat(foundOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(foundOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(foundOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(foundOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(foundOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(foundOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(foundOrder.getDeliveryAddress()).isEqualTo(order.getDeliveryAddress());
        assertThat(foundOrder.getPayment()).isEqualTo(order.getPayment());
        assertThat(foundOrder.getItems().size()).isEqualTo(1);
    }
}
