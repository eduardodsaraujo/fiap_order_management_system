package br.com.fiap.delivery_logistics.application.dto.shipping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CalculateShippingResponseDto {
    Double cost;
    Integer deliveryTime;
}
