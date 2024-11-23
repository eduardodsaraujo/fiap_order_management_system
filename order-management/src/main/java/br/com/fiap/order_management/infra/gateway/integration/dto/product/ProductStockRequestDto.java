package br.com.fiap.order_management.infra.gateway.integration.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductStockRequestDto {

    private long productId;
    private double quantity;

}
