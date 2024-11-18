package br.com.fiap.order_management.gateway.delivery;

import lombok.Getter;

@Getter
public class CalculateShippingResponseEntity {

    private Double cost;
    private Integer deliveryTime;

}
