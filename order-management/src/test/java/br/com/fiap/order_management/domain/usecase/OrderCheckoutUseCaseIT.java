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
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.stubbing.Scenario.STARTED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WireMockTest(httpPort = 8080)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class OrderCheckoutUseCaseIT {

    @Autowired
    private OrderCheckoutUseCase orderCheckoutUseCase;
    @Autowired
    private OrderGateway orderGateway;

    @Test
    public void shouldCheckoutOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        orderGateway.save(order);

        stubFor(put("/product-management/api/products/stock/decrease")
                .inScenario("checkout-order")
                .willReturn(ok()));

        PaymentInput input = new PaymentInput(PaymentMethod.PIX);

        // Act
        OrderOutput updatedOrder = orderCheckoutUseCase.execute(orderId, input);

        // Assert
        assertThat(updatedOrder.getId()).isEqualTo(order.getId());
        assertThat(updatedOrder.getStatus()).isEqualTo(OrderStatus.WAITING_PAYMENT);
        assertThat(updatedOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(updatedOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(updatedOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(updatedOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(updatedOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(updatedOrder.getDeliveryAddress().getId()).isEqualTo(order.getDeliveryAddress().getId());

    }

}