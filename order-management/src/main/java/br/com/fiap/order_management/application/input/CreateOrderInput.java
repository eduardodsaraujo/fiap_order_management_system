package br.com.fiap.order_management.application.input;

import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderInput {

    private long customerId;
    private List<OrderItemInput> items;

}
