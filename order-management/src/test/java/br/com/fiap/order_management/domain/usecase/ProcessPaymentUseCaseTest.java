package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.PaymentGateway;
import br.com.fiap.order_management.domain.gateway.StockProductGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.domain.model.PaymentReceipt;
import br.com.fiap.order_management.infra.exception.OrderException;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProcessPaymentUseCaseTest {

    private ProcessPaymentUseCase processPaymentUseCase;

    @Mock
    private OrderGateway orderGateway;
    @Mock
    private DeliveryGateway deliveryGateway;
    @Mock
    private PaymentGateway paymentGateway;
    @Mock
    private StockProductGateway stockProductGateway;
    AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        processPaymentUseCase = new ProcessPaymentUseCase(orderGateway, paymentGateway, deliveryGateway, stockProductGateway);
    }

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

        doNothing().when(deliveryGateway).createDelivery(any(UUID.class), anyString());
        when(paymentGateway.getPayment(any(UUID.class))).thenReturn(paymentReceipt);
        when(orderGateway.findByPaymentId(any(UUID.class))).thenReturn(order);
        when(orderGateway.save(any(Order.class))).thenReturn(order);

        // Act
        processPaymentUseCase.execute(paymentId);

        // Assert
        verify(deliveryGateway, times(1)).createDelivery(any(UUID.class), anyString());
        verify(paymentGateway, times(1)).getPayment(any(UUID.class));
        verify(orderGateway, times(1)).findByPaymentId(any(UUID.class));
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(OrderStatus.AWAITING_SHIPMENT).isEqualTo(order.getStatus());
        assertThat(order.getPayment().getStatus()).isEqualTo(paymentReceipt.getStatus());
    }

    @Test
    public void shouldCancelOrder_WhenProcessPayment_PaymentRejected() throws Exception {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        PaymentReceipt paymentReceipt = new PaymentReceipt(paymentId, LocalDateTime.now(), "REJECTED", UUID.randomUUID().toString());

        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(Payment.builder().id(paymentId).build());

        when(paymentGateway.getPayment(any(UUID.class))).thenReturn(paymentReceipt);
        doNothing().when(stockProductGateway).increaseStock(anyLong(), anyDouble());
        when(orderGateway.findByPaymentId(any(UUID.class))).thenReturn(order);
        when(orderGateway.save(any(Order.class))).thenReturn(order);

        // Act
        processPaymentUseCase.execute(paymentId);

        // Assert
        verify(paymentGateway, times(1)).getPayment(any(UUID.class));
        verify(stockProductGateway, times(1)).increaseStock(anyLong(), anyDouble());
        verify(orderGateway, times(1)).findByPaymentId(any(UUID.class));
        verify(orderGateway, times(1)).save(any(Order.class));

        assertThat(OrderStatus.CANCELED).isEqualTo(order.getStatus());
        assertThat(order.getPayment().getStatus()).isEqualTo(paymentReceipt.getStatus());
    }


    @Test
    public void shouldThrowException_WhenProcessPayment_RequestTwoTimes() {
        // Arrange
        UUID paymentId = UUID.randomUUID();

        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(Payment.builder().id(paymentId).status("REJECTED").build());

        when(orderGateway.findByPaymentId(any(UUID.class))).thenReturn(order);

        // Act
        // Assert
        assertThatThrownBy(
                () -> processPaymentUseCase.execute(paymentId))
                .isInstanceOf(OrderException.class)
                .hasMessage("Payment already processed.");
    }

}