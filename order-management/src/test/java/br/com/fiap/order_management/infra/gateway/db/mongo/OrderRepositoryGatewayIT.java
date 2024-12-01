package br.com.fiap.order_management.infra.gateway.db.mongo;


import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.util.CustomerHelper;
import br.com.fiap.order_management.util.OrderHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class OrderRepositoryGatewayIT {

    @Autowired
    private OrderRepositoryGateway orderRepositoryGateway;

    @Test
    public void shouldSaveOrder() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);

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
    }

    @Test
    public void shouldSaveOrderWithPayment() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(OrderHelper.createPayment());

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
    }

    @Test
    public void shouldFindOrderById() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        Order order = OrderHelper.createOrder();
        order.setId(orderId);
        order.setDeliveryAddress(CustomerHelper.createDeliveryAddress());
        order.setPayment(OrderHelper.createPayment());
        orderRepositoryGateway.save(order);

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
        order.getPayment().setId(paymentId);
        orderRepositoryGateway.save(order);

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
    }

    @Test
    public void shouldFindAllOrdersByCustomerId() {
        // Arrange
        Order order1 = OrderHelper.createOrder();
        Order order2 = OrderHelper.createOrder();
        Order order3 = OrderHelper.createOrder();
        orderRepositoryGateway.save(order1);
        orderRepositoryGateway.save(order2);
        orderRepositoryGateway.save(order3);

        long customerId = 1L;

        // Act
        var foundOrders = orderRepositoryGateway.findAllByCustomerId(customerId);

        // Assert
        assertThat(foundOrders).isNotNull().hasSizeGreaterThan(1);
    }

}