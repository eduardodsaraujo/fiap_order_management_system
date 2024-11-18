package br.com.fiap.order_management.gateway.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CalculateShippingRequestEntity {

    private String destinationZipCode;
    private Double weightProducts;

}
