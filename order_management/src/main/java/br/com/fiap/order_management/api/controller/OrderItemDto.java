package br.com.fiap.order_management.api.controller;

import br.com.fiap.order_management.domain.model.OrderItem;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class OrderItemDto {

    private double quantity;
    private ProductDto product;

    public static OrderItemDto toDto(OrderItem item){
        return OrderItemDto.builder()
                .quantity(item.getQuantity())
                .product(ProductDto.toDto(item.getProduct()))
                .build();
    }

    public static List<OrderItemDto> toListDto(List<OrderItem> list) {
        return list.stream().map(OrderItemDto::toDto).toList();
    }

}
