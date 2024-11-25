package br.com.fiap.order_management.domain.input;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateOrderInput {

    private long customerId;
    private List<OrderItemInput> items;

}
