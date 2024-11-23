package br.com.fiap.order_management.infra.gateway.integration.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculateShippingRequestDto {

    private String destinationZipCode;
    private Double weightProducts;

}
