package br.com.fiap.order_management.application.input;

import lombok.Getter;

@Getter
public class OrderItemInput {

    private long productId;
    private double quantity;

}
