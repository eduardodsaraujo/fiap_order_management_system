package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateShippingRequestDto {
    String destinationZipCode;
    Integer weightProducts;
}
