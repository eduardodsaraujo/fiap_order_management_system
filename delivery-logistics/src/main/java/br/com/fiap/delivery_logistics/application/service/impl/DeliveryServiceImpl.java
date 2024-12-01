package br.com.fiap.delivery_logistics.application.service.impl;

import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.DeliveryService;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.infrastructure.client.OrderManagementClient;
import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryRepository;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final OrderManagementClient orderManagementClient;
    private static final String ERROR_MESSAGE = "Delivery not found";
    public DeliveryResponseDto createDelivery(DeliveryRequestDto requestDto) {
        Delivery delivery = new Delivery();
        delivery.setOrderId(requestDto.getOrderId());
        delivery.setDestinationZipCode(requestDto.getDestinationZipCode());
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setLastUpdated(LocalDateTime.now());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return toDto(savedDelivery);
    }


    public DeliveryResponseDto saveDeliveryTrack(UUID orderId, BigDecimal latitude, BigDecimal longitude) {
        Delivery delivery = deliveryRepository.findByOrderId(orderId)
                .orElseThrow(() -> new DeliveryException(ERROR_MESSAGE));
        delivery.setLatitude(latitude);
        delivery.setLongitude(longitude);
        Delivery savedDelivery = deliveryRepository.save(delivery);
        return toDto(savedDelivery);
    }

    private DeliveryResponseDto toDto(Delivery savedDelivery) {
        DeliveryResponseDto responseDto = new DeliveryResponseDto();
        responseDto.setOrderId(savedDelivery.getOrderId());
        responseDto.setDestinationZipCode(savedDelivery.getDestinationZipCode());
        responseDto.setStatus(savedDelivery.getStatus());
        responseDto.setLatitude(savedDelivery.getLatitude());
        responseDto.setLongitude(savedDelivery.getLongitude());
        responseDto.setLastUpdated(savedDelivery.getLastUpdated());

        if (savedDelivery.getDeliveryPerson() != null) {
            DeliveryPersonResponseDto deliveryPersonResponseDto = new DeliveryPersonResponseDto();
            deliveryPersonResponseDto.setId(savedDelivery.getDeliveryPerson().getId());
            deliveryPersonResponseDto.setName(savedDelivery.getDeliveryPerson().getName());
            deliveryPersonResponseDto.setVehicleType(savedDelivery.getDeliveryPerson().getVehicleType());
            deliveryPersonResponseDto.setStatus(savedDelivery.getDeliveryPerson().getStatus());
            responseDto.setDeliveryPerson(deliveryPersonResponseDto);
        }

        return responseDto;
    }

    public DeliveryResponseDto getById(UUID deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(ERROR_MESSAGE));
        return toDto(delivery);
    }

    public DeliveryResponseDto changeDeliveryStatus(UUID deliveryId,
                                ChangeDeliveryStatusRequestDto changeDeliveryStatusRequestDto) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new DeliveryException(ERROR_MESSAGE));
        delivery.setStatus(changeDeliveryStatusRequestDto.getStatus());

        deliveryRepository.save(delivery);

        if(changeDeliveryStatusRequestDto.getStatus().equals(DeliveryStatus.DELIVERED)){
            orderManagementClient.updateDelivered(deliveryId);
        }

        return toDto(delivery);
    }

    public Page<DeliveryResponseDto> findAllDeliveries(Pageable pageable) {
        Page<Delivery> deveryPage = deliveryRepository.findAll(pageable);
        return deveryPage.map(this::toDto);
    }

}
