package br.com.fiap.order_management.infra.gateway.integration.mapper;

import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductDto;

public class ProductDtoMapper {

    public static Product toDomain(ProductDto productDto){
        return Product.builder()
                .id(productDto.getId())
                .code(productDto.getCode())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .weight(productDto.getWeight())
                .build();
    }

}
