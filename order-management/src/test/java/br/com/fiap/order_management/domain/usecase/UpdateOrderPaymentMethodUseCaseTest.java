package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.input.UpdatePaymentInput;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.domain.model.PaymentMethod;
import br.com.fiap.order_management.domain.output.OrderOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class UpdateOrderPaymentMethodUseCaseTest {

    @Mock
    private OrderGateway orderGateway;

    @InjectMocks
    private UpdateOrderPaymentMethodUseCase updateOrderPaymentMethodUseCase;

    private UUID orderId;
    private Order order;
    private UpdatePaymentInput input;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        order = mock(Order.class);
        input = UpdatePaymentInput.builder()
                .paymentMethod(PaymentMethod.PIX)
                .build();


        when(orderGateway.findById(orderId)).thenReturn(order);
        when(orderGateway.save(any(Order.class))).thenReturn(order);
    }

    @Test
    void update_ShouldUpdateOrderPaymentMethodAndReturnOrderOutput() {
        OrderOutput orderOutput = updateOrderPaymentMethodUseCase.update(orderId, input);
        verify(orderGateway, times(1)).findById(orderId);
        verify(order, times(1)).updatePayment(any(Payment.class));
        verify(orderGateway, times(1)).save(order);
        assertNotNull(orderOutput);
        Payment payment = order.getPayment();
        assertNotNull(payment);
        assertEquals("CREDIT_CARD", payment.getPaymentMethod());
    }
}
