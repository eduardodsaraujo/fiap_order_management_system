package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderDto {

    private UUID id;
    private LocalDate orderDate;
    private OrderStatus status;
    private double itemTotal;
    private double shippingValue;
    private double total;
    private CustomerDto customer;
    private PaymentDto payment;
    private List<OrderItemDto> items;

    public static OrderDto toDto(Order order){
        return OrderDto.builder()
                .id(order.getId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .itemTotal(order.getItemTotal())
                .shippingValue(order.getShippingValue())
                .total(order.getTotal())
                .customer(CustomerDto.toDto(order.getCustomer()))
                .payment(PaymentDto.toDto(order.getPayment()))
                .items(OrderItemDto.toListDto(order.getItems()))
                .build();
    }

}
