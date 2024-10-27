package br.com.fiap.product_management.application.input;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateProductInput {

    private String code;
    private String name;
    private String description;
    private String category;
    private String manufacturer;
    private double price;
    private double stockQuantity;

}
