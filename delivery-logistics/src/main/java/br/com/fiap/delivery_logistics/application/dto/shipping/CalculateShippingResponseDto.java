package br.com.fiap.delivery_logistics.application.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateShippingResponseDto {
    Double cost;
    Integer deliveryTime;
}
