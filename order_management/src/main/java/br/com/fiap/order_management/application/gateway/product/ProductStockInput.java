package br.com.fiap.order_management.application.gateway.product;

import br.com.fiap.order_management.domain.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductStockInput {

    private long id;
    private double quantity;

}
