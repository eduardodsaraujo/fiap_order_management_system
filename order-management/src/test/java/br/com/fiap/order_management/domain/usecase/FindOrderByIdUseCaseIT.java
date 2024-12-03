package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.output.OrderOutput;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class FindOrderByIdUseCaseIT {

    @Autowired
    private FindOrderByIdUseCase findOrderByIdUseCase;
    @Autowired
    private OrderGateway orderGateway;

    @Test
    void shouldFindOrderByIdUseCase() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        orderGateway.save(order);

        // Act
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
