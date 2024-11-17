package br.com.fiap.order_management.api.controller.dto;

import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private double totalWeight;
    private CustomerDto customer;
    private DeliveryAddressDto deliveryAddress;
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
                .totalWeight(order.getTotalWeight())
                .customer(CustomerDto.toDto(order.getCustomer()))
                .deliveryAddress(Optional.ofNullable(order.getDeliveryAddress()).stream().map(DeliveryAddressDto::toDto).findFirst().orElse(null))
                .payment(Optional.ofNullable(order.getPayment()).stream().map(PaymentDto::toDto).findFirst().orElse(null))
                .items(OrderItemDto.toListDto(order.getItems()))
                .build();
    }

    public static List<OrderDto> toListDto(List<Order> list) {
        return list.stream().map(OrderDto::toDto).toList();
    }

}
