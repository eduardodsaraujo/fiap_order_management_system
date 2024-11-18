package br.com.fiap.product_management.application.input;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateStockInput {

    private long productId;
    private double quantity;

}
