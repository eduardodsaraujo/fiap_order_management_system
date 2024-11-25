package br.com.fiap.order_management.domain.mapper;

import br.com.fiap.order_management.domain.model.*;
import br.com.fiap.order_management.domain.output.*;

public class OrderOutputMapper {

    public static OrderOutput toOrderOutput(Order order) {
        return OrderOutput.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .itemTotal(order.getItemTotal())
                .shippingValue(order.getShippingValue())
                .total(order.getTotal())
                .totalWeight(order.getTotalWeight())
                .customer(toCustomerOutput(order.getCustomer()))
                .deliveryAddress(toDeliveryAddressOutput(order.getDeliveryAddress()))
                .payment(toPaymentOutput(order.getPayment()))
                .items(order.getItems().stream().map(OrderOutputMapper::toOrderItemOutput).toList())
                .build();
    }

    private static CustomerOutput toCustomerOutput(Customer customer) {
        if (customer != null) {
            return CustomerOutput.builder()
                    .id(customer.getId())
                    .name(customer.getName())
                    .email(customer.getEmail())
                    .build();
        }
        return null;
    }

    private static DeliveryAddressOutput toDeliveryAddressOutput(DeliveryAddress address) {
        if (address != null) {
            return DeliveryAddressOutput.builder()
                    .id(address.getId())
                    .street(address.getStreet())
                    .number(address.getNumber())
                    .complement(address.getComplement())
                    .city(address.getCity())
                    .state(address.getState())
                    .postalCode(address.getPostalCode())
                    .build();
        }
        return null;
    }

    private static PaymentOutput toPaymentOutput(Payment payment) {
        if (payment != null) {
            return PaymentOutput.builder()
                    .id(payment.getId())
                    .paymentMethod(payment.getPaymentMethod())
                    .value(payment.getValue())
                    .authNumber(payment.getAuthNumber())
                    .paymentTimestamp(payment.getPaymentTimestamp())
                    .status(payment.getStatus())
                    .build();
        }
        return null;
    }

    private static OrderItemOutput toOrderItemOutput(OrderItem item) {
        return OrderItemOutput.builder()
                .quantity(item.getQuantity())
                .product(OrderOutputMapper.toProductOutput(item.getProduct()))
                .build();
    }

    private static ProductOutput toProductOutput(Product product) {
        return ProductOutput.builder()
                .id(product.getId())
                .code(product.getCode())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

}
