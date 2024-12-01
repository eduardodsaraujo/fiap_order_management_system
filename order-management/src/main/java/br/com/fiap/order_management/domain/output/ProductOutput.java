package br.com.fiap.order_management.domain.output;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductOutput {

    private long id;
    private String code;
    private String description;
    private double price;
    private double weight;

}
