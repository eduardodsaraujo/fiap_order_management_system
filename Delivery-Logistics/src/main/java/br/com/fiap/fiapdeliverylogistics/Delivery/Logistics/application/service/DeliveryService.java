package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.delivery.DeliveryRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.delivery.DeliveryResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.Delivery;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryStatus;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.repository.DeliveryRepository;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.infrastructure.exception.DeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private static final String ERROR_MESSAGE = "Delivery not found";
    public DeliveryResponseDto createDelivery(DeliveryRequestDto requestDto) {
        Delivery delivery = new Delivery();
//        delivery.setOrderId(requestDto.getOrderId()); TODO FIX
        delivery.setDestinationZipCode(requestDto.getDestinationZipCode());
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setLastUpdated(LocalDateTime.now());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return toDto(savedDelivery);
    }


    public DeliveryResponseDto saveDeliveryTrack(Long orderId, BigDecimal latitude, BigDecimal longitude) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryException(ERROR_MESSAGE));
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
                .orElseThrow(() -> new DeliveryException(ERROR_MESSAGE));
        return toDto(delivery);
    }

    public DeliveryResponseDto changeDeliveryStatus(Long deliveryId,
                                ChangeDeliveryStatusRequestDto changeDeliveryStatusRequestDto) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(ERROR_MESSAGE));
        delivery.setStatus(changeDeliveryStatusRequestDto.getStatus());
        deliveryRepository.save(delivery);
        return toDto(delivery);
    }

    public Page<DeliveryResponseDto> findAllDeliveries(Pageable pageable) {
        Page<Delivery> deveryPage = deliveryRepository.findAll(pageable);
        return deveryPage.map(this::toDto);
    }

}
