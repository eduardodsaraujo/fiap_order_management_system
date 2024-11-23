package br.com.fiap.order_management.infra.gateway.integration.dto.product;

import lombok.Getter;

@Getter
public class ProductDto {

    private long id;
    private String code;
    private String description;
    private double price;
    private double weight;

}
