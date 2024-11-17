package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.domain.model.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductDto {

    private long id;
    private String code;
    private String name;
    private String description;
    private String category;
    private String manufacturer;
    private boolean enable;
    private double price;
    private double weight;

    public static ProductDto toDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .description(product.getDescription())
                .category(product.getCategory())
                .manufacturer(product.getManufacturer())
                .enable(product.isEnable())
                .price(product.getPrice())
                .weight(product.getWeight())
                .build();
    }

    public static List<ProductDto> toListDto(List<Product> list) {
        return list.stream().map(ProductDto::toDto).toList();
    }

}
