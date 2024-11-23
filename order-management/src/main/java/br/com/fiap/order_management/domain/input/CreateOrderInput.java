package br.com.fiap.order_management.domain.input;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderInput {

    private long customerId;
    private List<OrderItemInput> items;

}
