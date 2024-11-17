package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document
public class Product {

    @Id
    private long id;
    private String code;
    private String description;
    private double price;
    private double weight;

}
