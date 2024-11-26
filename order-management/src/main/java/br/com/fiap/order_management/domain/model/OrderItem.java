package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderItem {

    private UUID id;
    private double quantity;
    private Product product;

    public OrderItem(UUID id, double quantity, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }

}
