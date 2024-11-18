package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;

public interface CalculateShippingService {
    public CalculateShippingResponseDto calculateShipping(CalculateShippingRequestDto requestDto);
}
