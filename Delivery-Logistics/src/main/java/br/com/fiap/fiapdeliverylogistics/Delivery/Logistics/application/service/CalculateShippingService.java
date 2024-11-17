package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.shipping.CalculateShippingResponseDto;

public interface CalculateShippingService {
    public CalculateShippingResponseDto calculateShipping(CalculateShippingRequestDto requestDto);
}
