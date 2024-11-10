package br.com.fiap.order_management.application.gateway.product;

import br.com.fiap.order_management.domain.model.Product;
import lombok.Getter;

@Getter
public class ProductEntity {

    private long id;
    private String code;
    private String description;
    private double price;

    public Product dtoToObject(){
        return Product.builder()
                .id(this.id)
                .code(this.code)
                .description(this.description)
                .price(this.price)
                .build();
    }

}
