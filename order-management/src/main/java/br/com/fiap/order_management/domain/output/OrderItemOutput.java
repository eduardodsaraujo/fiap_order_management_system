package br.com.fiap.order_management.domain.output;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class OrderItemOutput {

    private UUID id;
    private double quantity;
    private ProductOutput product;

}
