package br.com.fiap.order_management.domain.model;

import br.com.fiap.order_management.infra.exception.OrderException;
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
        this.status = OrderStatus.OPEN;

        validateHasItems();
        calculateTotal();
        calculateTotalWeight();
    }

    private void validateHasItems() {
        if (items.isEmpty()) {
            throw new OrderException("The order does not contain items.");
        }
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
        this.status = OrderStatus.WAITING_PAYMENT;
    }

    public void updateAwaitingShipment() {
        this.status = OrderStatus.AWAITING_SHIPMENT;
    }

    public void updateDelivered() {
        this.status = OrderStatus.DELIVERED;
    }

    public void processPayment(PaymentReceipt paymentReceipt) {
        payment.setPaymentTimestamp(paymentReceipt.getPaymentTimestamp());
        payment.setStatus(paymentReceipt.getStatus());
        payment.setAuthNumber(paymentReceipt.getAuthNumber());

        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELED;
    }

    public void validateIsOpen() {
        if (status != OrderStatus.OPEN) {
            throw new OrderException("The order is not open.");
        }
    }

    public void validateDeliveryAddress() {
        if (deliveryAddress == null) {
            throw new OrderException("Invalid delivery address.");
        }
    }

}
