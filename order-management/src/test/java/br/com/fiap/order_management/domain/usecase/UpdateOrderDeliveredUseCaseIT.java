package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WireMockTest(httpPort = 8080)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class UpdateOrderDeliveredUseCaseIT {

    @Autowired
    private UpdateOrderDeliveredUseCase updateOrderDeliveredUseCase;
    @Autowired
    private OrderGateway orderGateway;

    @Test
    public void shouldUpdateOrderDelivered() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        orderGateway.save(order);

        // Act
        updateOrderDeliveredUseCase.execute(orderId);
        Order foundOrder = orderGateway.findById(orderId);

        // Assert
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.DELIVERED);
    }

}
