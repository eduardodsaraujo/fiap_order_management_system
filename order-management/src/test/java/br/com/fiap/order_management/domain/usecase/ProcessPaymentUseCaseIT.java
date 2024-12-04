package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.domain.model.PaymentReceipt;
import br.com.fiap.order_management.infra.exception.OrderException;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.OrderHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@WireMockTest(httpPort = 8080)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class ProcessPaymentUseCaseIT {

    @Autowired
    private ProcessPaymentUseCase processPaymentUseCase;

    @Autowired
    private OrderGateway orderGateway;

    @Test
    public void shouldProcessPayment() throws Exception {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        PaymentReceipt paymentReceipt = new PaymentReceipt(paymentId, LocalDateTime.now(), "PAID", UUID.randomUUID().toString());

        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(Payment.builder().id(paymentId).build());
        orderGateway.save(order);

        stubFor(post("/delivery-logistics/api/delivery").willReturn(ok()));

        // Act
        processPaymentUseCase.execute(paymentId);
        Order foundOrder = orderGateway.findById(orderId);

        // Assert
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.AWAITING_SHIPMENT);
        assertThat(foundOrder.getPayment().getStatus()).isEqualTo(paymentReceipt.getStatus());
    }

}