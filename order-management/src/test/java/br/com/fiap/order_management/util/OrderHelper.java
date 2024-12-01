package br.com.fiap.order_management.util;

import br.com.fiap.order_management.domain.input.CreateOrderInput;
import br.com.fiap.order_management.domain.input.OrderItemInput;
import br.com.fiap.order_management.domain.model.*;
import br.com.fiap.order_management.domain.output.*;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.OrderDocument;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.OrderItemDocument;
import br.com.fiap.order_management.infra.gateway.db.mongo.document.PaymentDocument;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderHelper {

    public static CreateOrderInput createOrderInput() {
        OrderItemInput itemInput = OrderItemInput.builder()
                .quantity(2.0)
                .productId(1L)
                .build();

        return CreateOrderInput.builder()
                .customerId(1L)
                .items(List.of(itemInput))
                .build();
    }

    public static OrderOutput createOrderOutput() {
        OrderItemOutput item = OrderItemOutput.builder()
                .id(UUID.randomUUID())
                .quantity(2.0)
                .product(ProductHelper.createProductOutput())
                .build();

        return OrderOutput.builder()
                .id(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.OPEN)
                .itemTotal(16000.00)
                .total(16000.00)
                .totalWeight(1.0)
                .customer(CustomerHelper.createCustomerOutput())
                .items(List.of(item))
                .build();
    }

    public static Order createOrder() {
        OrderItem item = OrderItem.builder()
                .id(UUID.randomUUID())
                .quantity(2.0)
                .product(ProductHelper.createProduct())
                .build();

        return Order.builder()
                .id(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.OPEN)
                .itemTotal(16000.00)
                .total(16000.00)
                .totalWeight(1.0)
                .customer(CustomerHelper.createCustomer())
                .items(List.of(item))
                .build();
    }

    public static Payment createPayment() {
        return Payment.builder()
                .id(UUID.fromString("84aa536f-1a4e-4097-ad8d-d66286673e01"))
                .status("PAID")
                .paymentMethod(PaymentMethod.PIX)
                .paymentTimestamp(LocalDateTime.now())
                .value(16000.00)
                .authNumber(UUID.randomUUID().toString())
                .build();
    }



    public static OrderDocument createOrderDocument() {
        OrderItemDocument item = OrderItemDocument.builder()
                .id(UUID.randomUUID())
                .quantity(2.0)
                .product(ProductHelper.createProductDocument())
                .build();

        return OrderDocument.builder()
                .id(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.OPEN)
                .itemTotal(16000.00)
                .total(16000.00)
                .totalWeight(1.0)
                .customer(CustomerHelper.createCustomerDocument())
                .items(List.of(item))
                .build();
    }

    public static PaymentDocument createPaymentDocument() {
        return PaymentDocument.builder()
                .id(UUID.fromString("84aa536f-1a4e-4097-ad8d-d66286673e01"))
                .status("PAID")
                .paymentMethod(PaymentMethod.PIX)
                .paymentTimestamp(LocalDateTime.now())
                .value(16000.00)
                .authNumber(UUID.randomUUID().toString())
                .build();
    }

}
