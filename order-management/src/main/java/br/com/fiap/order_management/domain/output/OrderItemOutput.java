package br.com.fiap.order_management.domain.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemOutput {

    private double quantity;
    private ProductOutput product;

}
