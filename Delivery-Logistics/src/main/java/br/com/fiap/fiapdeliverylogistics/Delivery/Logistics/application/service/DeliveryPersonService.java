package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
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
