package br.com.fiap.order_management.infra.gateway.db.mongo;


import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.OrderDocument;
import br.com.fiap.order_management.infra.gateway.db.mongo.repository.OrderRepository;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class OrderRepositoryGatewayTest {
    
    private OrderRepositoryGateway orderRepositoryGateway;
    
    @Mock
    private OrderRepository orderRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        orderRepositoryGateway = new OrderRepositoryGateway(orderRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        if (openMocks != null) {
            openMocks.close();
        }
    }

    @Test
    public void shouldSaveOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);

        OrderDocument orderDocument = OrderHelper.createOrderDocument();
        orderDocument.setId(orderId);

        when(orderRepository.save(any(OrderDocument.class))).thenReturn(orderDocument);

        // Act
        var savedOrder = orderRepositoryGateway.save(order);

        // Assert
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(savedOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(savedOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(savedOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(savedOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(savedOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(savedOrder.getDeliveryAddress()).isEqualTo(order.getDeliveryAddress());
        assertThat(savedOrder.getPayment()).isEqualTo(order.getPayment());
        verify(orderRepository, times(1)).save(any(OrderDocument.class));
    }

    @Test
    public void shouldSaveOrderWithPayment() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(OrderHelper.createPayment());

        OrderDocument orderDocument = OrderHelper.createOrderDocument();
        orderDocument.setId(orderId);
        orderDocument.setDeliveryAddress(CustomerHelper.createDeliveryAddressDocument());
        orderDocument.setPayment(OrderHelper.createPaymentDocument());

        when(orderRepository.save(any(OrderDocument.class))).thenReturn(orderDocument);

        // Act
        var savedOrder = orderRepositoryGateway.save(order);

        // Assert
        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getId()).isEqualTo(order.getId());
        assertThat(savedOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(savedOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(savedOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(savedOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(savedOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(savedOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(savedOrder.getDeliveryAddress().getId()).isEqualTo(order.getDeliveryAddress().getId());
        assertThat(savedOrder.getPayment().getId()).isEqualTo(order.getPayment().getId());
        verify(orderRepository, times(1)).save(any(OrderDocument.class));
    }

    @Test
    public void shouldFindOrderById() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(OrderHelper.createPayment());

        OrderDocument orderDocument = OrderHelper.createOrderDocument();
        orderDocument.setId(orderId);
        orderDocument.setDeliveryAddress(CustomerHelper.createDeliveryAddressDocument());
        orderDocument.setPayment(OrderHelper.createPaymentDocument());

        when(orderRepository.findById(any(UUID.class))).thenReturn(Optional.of(orderDocument));

        // Act
        var foundOrder = orderRepositoryGateway.findById(orderId);

        // Assert
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
        assertThat(foundOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(foundOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(foundOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(foundOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(foundOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(foundOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(foundOrder.getDeliveryAddress().getId()).isEqualTo(order.getDeliveryAddress().getId());
        assertThat(foundOrder.getPayment().getId()).isEqualTo(order.getPayment().getId());
        verify(orderRepository, times(1)).findById(any(UUID.class));
    }


    @Test
    public void shouldFindOrderByPaymentId() {
        // Arrange
        UUID paymentId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(OrderHelper.createPayment());

        OrderDocument orderDocument = OrderHelper.createOrderDocument();
        orderDocument.setId(orderId);
        orderDocument.setDeliveryAddress(CustomerHelper.createDeliveryAddressDocument());
        orderDocument.setPayment(OrderHelper.createPaymentDocument());

        when(orderRepository.findByPaymentId(any(UUID.class))).thenReturn(Optional.of(orderDocument));

        // Act
        var foundOrder = orderRepositoryGateway.findByPaymentId(paymentId);

        // Assert
        assertThat(foundOrder).isNotNull();
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
        assertThat(foundOrder.getId()).isEqualTo(order.getId());
        assertThat(foundOrder.getStatus()).isEqualTo(order.getStatus());
        assertThat(foundOrder.getItemTotal()).isEqualTo(order.getItemTotal());
        assertThat(foundOrder.getShippingValue()).isEqualTo(order.getShippingValue());
        assertThat(foundOrder.getTotal()).isEqualTo(order.getTotal());
        assertThat(foundOrder.getTotalWeight()).isEqualTo(order.getTotalWeight());
        assertThat(foundOrder.getCustomer().getId()).isEqualTo(order.getCustomer().getId());
        assertThat(foundOrder.getDeliveryAddress().getId()).isEqualTo(order.getDeliveryAddress().getId());
        assertThat(foundOrder.getPayment().getId()).isEqualTo(order.getPayment().getId());
        verify(orderRepository, times(1)).findByPaymentId(any(UUID.class));
    }

    @Test
    public void shouldFindAllOrdersByCustomerId() {
        // Arrange
        long customerId = 1L;
        OrderDocument order1 = OrderHelper.createOrderDocument();
        OrderDocument order2 = OrderHelper.createOrderDocument();
        var orders = Arrays.asList(order1, order2);

        when(orderRepository.findAllByCustomerId(anyLong())).thenReturn(orders);

        // Act
        var foundOrders = orderRepositoryGateway.findAllByCustomerId(customerId);

        // Assert
        assertThat(foundOrders).isNotNull().hasSize(2);
        verify(orderRepository, times(1)).findAllByCustomerId(anyLong());
    }

}