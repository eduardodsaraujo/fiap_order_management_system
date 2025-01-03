package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Product {

    private long id;
    private String code;
    private String description;
    private double price;
    private double weight;

}
