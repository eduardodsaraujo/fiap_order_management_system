package br.com.fiap.order_management.infra.gateway.integration.dto.product;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private long id;
    private String code;
    private String description;
    private double price;
    private double weight;

}
