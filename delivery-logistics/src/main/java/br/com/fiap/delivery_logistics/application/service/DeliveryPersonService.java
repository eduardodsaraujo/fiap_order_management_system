package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.SortedMap;

public interface DeliveryPersonService {

    DeliveryPersonResponseDto createDeliveryPerson(DeliveryPersonRequestDto deliveryPersonRequestDto);

    void assignAvailableDeliveryPeopleToPendingDeliveries();

    SortedMap<String, List<DeliveryResponseDto>> calculateRoutesByRegionResponse();

    Page<DeliveryPersonResponseDto> findAllDeliveryPeople(Pageable pageable);

    DeliveryPersonResponseDto changeDeliveryPersonStatus(Long deliveryPersonId, ChangeDeliveryPersonStatusRequestDto requestDto);

}
