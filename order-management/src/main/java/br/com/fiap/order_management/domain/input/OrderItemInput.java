package br.com.fiap.order_management.domain.input;

import lombok.Data;

@Data
public class OrderItemInput {

    private long productId;
    private double quantity;

}
