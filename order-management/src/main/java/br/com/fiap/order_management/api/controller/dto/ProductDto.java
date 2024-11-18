package br.com.fiap.order_management.api.controller.dto;

import br.com.fiap.order_management.domain.model.Product;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDto {

    private long id;
    private String code;
    private String description;
    private double price;

    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

}
