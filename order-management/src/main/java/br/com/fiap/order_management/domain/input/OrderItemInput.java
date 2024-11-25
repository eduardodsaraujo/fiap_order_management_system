package br.com.fiap.order_management.domain.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemInput {

    private long productId;
    private double quantity;

}
