package br.com.fiap.order_management.domain.output;

import br.com.fiap.order_management.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderOutput {

    private UUID id;
    private LocalDate orderDate;
    private OrderStatus status;
    private double itemTotal;
    private double shippingValue;
    private double total;
    private double totalWeight;
    private CustomerOutput customer;
    private DeliveryAddressOutput deliveryAddress;
    private PaymentOutput payment;
    private List<OrderItemOutput> items;

}
