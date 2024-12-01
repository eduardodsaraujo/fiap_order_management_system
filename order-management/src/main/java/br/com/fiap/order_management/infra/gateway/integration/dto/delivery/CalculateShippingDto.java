package br.com.fiap.order_management.infra.gateway.integration.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateShippingDto {

    private Double cost;
    private Integer deliveryTime;

}
