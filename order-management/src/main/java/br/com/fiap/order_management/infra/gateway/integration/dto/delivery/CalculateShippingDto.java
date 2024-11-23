package br.com.fiap.order_management.infra.gateway.integration.dto.delivery;

import lombok.Getter;

@Getter
public class CalculateShippingDto {

    private Double cost;
    private Integer deliveryTime;

}
