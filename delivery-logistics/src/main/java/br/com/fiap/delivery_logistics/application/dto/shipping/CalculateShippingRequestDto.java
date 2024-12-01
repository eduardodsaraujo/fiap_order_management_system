package br.com.fiap.delivery_logistics.application.dto.shipping;

import lombok.*;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CalculateShippingRequestDto {
    String destinationZipCode;
    Integer weightProducts;
}
