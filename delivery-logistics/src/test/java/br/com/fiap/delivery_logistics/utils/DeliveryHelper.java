package br.com.fiap.delivery_logistics.utils;

import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryTrackRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingRequestDto;
import br.com.fiap.delivery_logistics.application.dto.shipping.CalculateShippingResponseDto;
import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryHelper {
    public static Delivery createDelivery(DeliveryStatus status) {
        return createDelivery(status, UUID.fromString("916d6109-ec12-4220-b331-a7f06e62a4ee"));
    }
    public static Delivery createDelivery(DeliveryStatus status, UUID orderId) {
        return Delivery.builder()
                .orderId(orderId)
                .destinationZipCode("12345678")
                .status(status)
                .latitude(BigDecimal.valueOf(40.7128))
                .longitude(BigDecimal.valueOf(-74.0060))
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    public static DeliveryResponseDto createDeliveryResponseDto(DeliveryStatus status) {
        return DeliveryResponseDto.builder()
                .orderId(UUID.fromString("916d6109-ec12-4220-b331-a7f06e62a4ee"))
                .destinationZipCode("12345678")
                .status(status)
                .latitude(BigDecimal.valueOf(40.7128))
                .longitude(BigDecimal.valueOf(-74.0060))
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    public static DeliveryRequestDto createDeliveryRequestDto() {
        return DeliveryRequestDto.builder()
                .orderId(UUID.fromString("916d6109-ec12-4220-b331-a7f06e62a4ee"))
                .destinationZipCode("12345678")
                .build();
    }

    public static DeliveryTrackRequestDto createDeliveryTrackRequestDto(){
        return DeliveryTrackRequestDto.builder()
                .orderId(UUID.fromString("916d6109-ec12-4220-b331-a7f06e62a4ee"))
                .latitude(BigDecimal.valueOf(40.7128))
                .longitude(BigDecimal.valueOf(-74.0060))
                .build();
    }

    public static CalculateShippingRequestDto createCalculateShippingRequestDto(){
        return CalculateShippingRequestDto.builder()
                .weightProducts(5)
                .destinationZipCode("12345678")
                .build();
    }

    public static CalculateShippingResponseDto createCalculateShippingResponseDto(){
        return CalculateShippingResponseDto.builder()
                .cost(5D)
                .deliveryTime(6)
                .build();
    }
}
