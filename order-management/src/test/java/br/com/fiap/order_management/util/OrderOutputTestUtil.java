package br.com.fiap.order_management.util;

import br.com.fiap.order_management.domain.model.OrderStatus;
import br.com.fiap.order_management.domain.model.PaymentMethod;
import br.com.fiap.order_management.domain.output.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class OrderOutputTestUtil {

    public static OrderOutput createOrderOutput() {
        return OrderOutput.builder()
                .id(UUID.randomUUID())
                .orderDate(LocalDate.now())
                .status(OrderStatus.CLOSED)
                .itemTotal(200.0)
                .shippingValue(20.0)
                .total(220.0)
                .totalWeight(5.0)
                .customer(createCustomerOutput())
                .deliveryAddress(createDeliveryAddressOutput())
                .payment(createPaymentOutput())
                .items(List.of(createOrderItemOutput()))
                .build();
    }

    private static CustomerOutput createCustomerOutput() {
        return CustomerOutput.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .phone("123-456-7890")
                .build();
    }

    private static DeliveryAddressOutput createDeliveryAddressOutput() {
        return DeliveryAddressOutput.builder()
                .id(1L)
                .street("123 Main St")
                .number("10A")
                .complement("Apt 1")
                .district("Downtown")
                .city("Sample City")
                .state("SC")
                .postalCode("12345678")
                .build();
    }

    private static PaymentOutput createPaymentOutput() {
        return PaymentOutput.builder()
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .value(220.0)
                .build();
    }

    private static OrderItemOutput createOrderItemOutput() {
        return OrderItemOutput.builder()
                .quantity(2.0)
                .product(createProductOutput())
                .build();
    }

    private static ProductOutput createProductOutput() {
        return ProductOutput.builder()
                .id(1L)
                .code("P123")
                .description("Sample Product")
                .price(100.0)
                .build();
    }
}
