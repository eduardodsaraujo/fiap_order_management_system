package br.com.fiap.delivery_logistics.application.service.impl;

import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.DeliveryPersonService;
import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPerson;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryPersonRepository;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryRepository;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DeliveryPersonServiceImpl implements DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryRepository deliveryRepository;

    public DeliveryPersonResponseDto createDeliveryPerson(DeliveryPersonRequestDto deliveryPersonRequestDto) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryPersonRequestDto.getNome(),
                deliveryPersonRequestDto.getVehicleType());
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);
        DeliveryPerson savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        return toDto(savedDeliveryPerson);
    }

    private DeliveryPersonResponseDto toDto(DeliveryPerson deliveryPerson) {
        DeliveryPersonResponseDto responseDto = new DeliveryPersonResponseDto();
        responseDto.setId(deliveryPerson.getId());
        responseDto.setNome(deliveryPerson.getNome());
        responseDto.setStatus(deliveryPerson.getStatus());
        responseDto.setVehicleType(deliveryPerson.getVehicleType());
        return responseDto;
    }

    @Transactional
    public void assignAvailableDeliveryPeopleToPendingDeliveries() {
        // Buscar entregadores disponíveis
        List<DeliveryPerson> availableDeliveryPersons = deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE);


        // Verifica se há entregadores disponíveis
        if (availableDeliveryPersons.isEmpty()) {
            throw new DeliveryException("Not  available delivery persons.");
        }

        SortedMap<String, List<Delivery>> pendingDeliveries = calculateRoutesByRegion();
        int regionsByDeliveryPerson = pendingDeliveries.size()/availableDeliveryPersons.size();

        int count = 0;
        int deliveryPersonCounter = 0;

        for(List<Delivery> deliveries: pendingDeliveries.values()){
            DeliveryPerson deliveryPerson = availableDeliveryPersons.get(deliveryPersonCounter);
            deliveries = deliveries.stream().map(d-> setDeliveryPerson(d, deliveryPerson)).toList();

            count++;
            if(count >= regionsByDeliveryPerson && deliveryPersonCounter < availableDeliveryPersons.size()  - 1) {
                count = 0;
                deliveryPersonCounter++;
            }
            deliveryRepository.saveAll(deliveries);
        }

    }

    private Delivery setDeliveryPerson(Delivery delivery, DeliveryPerson deliveryPerson){
        delivery.setDeliveryPerson(deliveryPerson);
        delivery.setStatus(DeliveryStatus.WAITING_FOR_PICKUP);
        delivery.setLastUpdated(LocalDateTime.now());
        return delivery;
    }

    public SortedMap<String, List<Delivery>> calculateRoutesByRegion() {
        // Only consider deliveries with "PENDING" status
        List<Delivery> deliveries = deliveryRepository.findByStatus(DeliveryStatus.PENDING);

        TreeMap<String, List<Delivery>> deliveriesByRegion = new TreeMap<>();

        for (Delivery delivery : deliveries) {
            // Get the region by the first 5 digits of the ZIP code
            String region = getRegionByZip(delivery.getDestinationZipCode());
            deliveriesByRegion
                    .computeIfAbsent(region, k -> new ArrayList<>())
                    .add(delivery);
        }

        return deliveriesByRegion;
    }

    public SortedMap<String, List<DeliveryResponseDto>> calculateRoutesByRegionResponse(){
        SortedMap<String, List<Delivery>> routes = calculateRoutesByRegion();
        SortedMap<String, List<DeliveryResponseDto>> routesDto = new TreeMap<>();

        for (Map.Entry<String, List<Delivery>> entry : routes.entrySet()) {
            String region = entry.getKey();
            List<Delivery> deliveries = entry.getValue();

            // Convert List<Delivery> to List<DeliveryDto>
            List<DeliveryResponseDto> deliveryDtos = deliveries.stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            routesDto.put(region, deliveryDtos);
        }

        return routesDto;

    }
    private DeliveryResponseDto toDto(Delivery savedDelivery) {
        DeliveryResponseDto responseDto = new DeliveryResponseDto();
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


    private String getRegionByZip(String zipCode) {
        // Get the first 5 digits of the ZIP code and map it to a region
        String zipPrefix = zipCode.substring(0, 5); // Get the first 5 digits of the zip code
        return "region " + zipPrefix; // Simple mapping based on the first 5 digits of the ZIP code
    }

    public Page<DeliveryPersonResponseDto> findAllDeliveryPeople(Pageable pageable) {
        Page<DeliveryPerson> deveryPesrsonPage = deliveryPersonRepository.findAll(pageable);
        return deveryPesrsonPage.map(this::toDto);
    }

    public DeliveryPersonResponseDto changeDeliveryPersonStatus(Long deliveryPersonId, ChangeDeliveryPersonStatusRequestDto requestDto) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findById(deliveryPersonId)
                .orElseThrow(() -> new DeliveryException("Delivery Person not found"));
        deliveryPerson.setStatus(requestDto.getStatus());
        deliveryPersonRepository.save(deliveryPerson);
        return toDto(deliveryPerson);

    }
}