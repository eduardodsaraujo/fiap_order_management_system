package br.com.fiap.order_management.gateway.product;

import br.com.fiap.order_management.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductStockInput {

    private long productId;
    private double quantity;

}
