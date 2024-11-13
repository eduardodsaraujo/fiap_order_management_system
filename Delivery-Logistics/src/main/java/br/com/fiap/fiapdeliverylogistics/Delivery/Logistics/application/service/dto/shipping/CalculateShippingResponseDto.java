package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.shipping;

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
