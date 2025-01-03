package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface DeliveryService {

    DeliveryResponseDto createDelivery(DeliveryRequestDto requestDto);

    DeliveryResponseDto saveDeliveryTrack(UUID orderId, BigDecimal latitude, BigDecimal longitude);

    DeliveryResponseDto getById(UUID deliveryId);

    DeliveryResponseDto changeDeliveryStatus(UUID deliveryId, ChangeDeliveryStatusRequestDto changeDeliveryStatusRequestDto);

    Page<DeliveryResponseDto> findAllDeliveries(Pageable pageable);
}