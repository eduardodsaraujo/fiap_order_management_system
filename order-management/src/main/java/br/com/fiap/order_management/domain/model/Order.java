package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class Order {

    private UUID id;
    private LocalDate orderDate;
    private OrderStatus status;
    private double itemTotal;
    private double shippingValue;
    private double total;
    private double totalWeight;
    private Customer customer;
    private DeliveryAddress deliveryAddress;
    private Payment payment;
    private List<OrderItem> items;

    public void create() {
        this.id = UUID.randomUUID();
        this.orderDate = LocalDate.now();
        this.status = OrderStatus.NEW;

        calculateTotal();
        calculateTotalWeight();
    }

    private void calculateTotal() {
        itemTotal = items.stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice()).sum();
        total = itemTotal + shippingValue;
    }

    private void calculateTotalWeight() {
        totalWeight = items.stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getWeight()).sum();
    }

    public void updateDeliveryAddress(DeliveryAddress deliveryAddress, double shippingValue) {
        this.deliveryAddress = deliveryAddress;
        this.shippingValue = shippingValue;

        this.calculateTotal();
    }

    public void updatePayment(Payment payment) {
        this.payment = payment;
    }

    public void updateDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    public void process() {
        this.status = OrderStatus.CLOSED;
    }

    public void processPayment() {
        this.status = OrderStatus.PAID;
    }

}