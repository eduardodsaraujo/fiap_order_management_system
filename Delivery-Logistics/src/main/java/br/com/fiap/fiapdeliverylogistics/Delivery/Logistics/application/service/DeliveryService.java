package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.ChangeDeliveryStatusRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.DeliveryPersonResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.DeliveryResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.Delivery;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryStatus;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.repository.DeliveryRepository;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.infrastructure.exception.DeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public void createDelivery(Long orderId) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setLastUpdated(LocalDateTime.now());
        deliveryRepository.save(delivery);
    }


    public DeliveryResponseDto saveDeliveryTrack(Long orderId, BigDecimal latitude, BigDecimal longitude) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryException("Delivery not found"));
        delivery.setLatitude(latitude);
        delivery.setLongitude(longitude);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return toDto(savedDelivery);
    }

    private DeliveryResponseDto toDto(Delivery savedDelivery) {
        DeliveryResponseDto responseDto = new DeliveryResponseDto();
        responseDto.setId(savedDelivery.getId());
        responseDto.setOrderId(savedDelivery.getOrderId());
        responseDto.setStatus(savedDelivery.getStatus());
        responseDto.setLatitude(savedDelivery.getLatitude());
        responseDto.setLongitude(savedDelivery.getLongitude());
        responseDto.setLastUpdated(savedDelivery.getLastUpdated());

        if (savedDelivery.getDeliveryPerson() != null) {
            DeliveryPersonResponseDto deliveryPersonResponseDto = new DeliveryPersonResponseDto();
            deliveryPersonResponseDto.setId(savedDelivery.getDeliveryPerson().getId());
            deliveryPersonResponseDto.setNome(savedDelivery.getDeliveryPerson().getNome());
            deliveryPersonResponseDto.setVehicleType(savedDelivery.getDeliveryPerson().getVehicleType());
            deliveryPersonResponseDto.setStatus(savedDelivery.getDeliveryPerson().getStatus());
            responseDto.setDeliveryPerson(deliveryPersonResponseDto);
        }

        return responseDto;
    }

    public DeliveryResponseDto getById(Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException("Delivery not found"));
        return toDto(delivery);
    }

    public DeliveryResponseDto changeDeliveryStatus(Long deliveryId,
                                ChangeDeliveryStatusRequestDto changeDeliveryStatusRequestDto) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException("Delivery not found"));
        delivery.setStatus(changeDeliveryStatusRequestDto.getStatus());
        deliveryRepository.save(delivery);
        return toDto(delivery);
    }
}
