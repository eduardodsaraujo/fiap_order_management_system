package br.com.fiap.delivery_logistics.application.service;


import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.DeliveryPersonServiceImpl;
import br.com.fiap.delivery_logistics.domain.model.*;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryPersonRepository;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeliveryPersonServiceImplTest {

    @InjectMocks
    private DeliveryPersonServiceImpl deliveryPersonService;

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateDeliveryPerson() {
        DeliveryPersonRequestDto requestDto = new DeliveryPersonRequestDto("John Doe", VehicleType.MOTORCYCLE);
        DeliveryPerson savedDeliveryPerson = new DeliveryPerson("John Doe", VehicleType.MOTORCYCLE);
        savedDeliveryPerson.setId(1L);
        savedDeliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(savedDeliveryPerson);

        DeliveryPersonResponseDto responseDto = deliveryPersonService.createDeliveryPerson(requestDto);

        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getId());
        assertEquals("John Doe", responseDto.getNome());
        assertEquals(VehicleType.MOTORCYCLE, responseDto.getVehicleType());
        assertEquals(DeliveryPersonStatus.AVAILABLE, responseDto.getStatus());

        verify(deliveryPersonRepository, times(1)).save(any(DeliveryPerson.class));
    }

    @Test
    void testAssignAvailableDeliveryPeopleToPendingDeliveries() {
        DeliveryPerson availablePerson = new DeliveryPerson("John Doe", VehicleType.MOTORCYCLE);
        availablePerson.setId(1L);
        availablePerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        List<DeliveryPerson> availableDeliveryPersons = Collections.singletonList(availablePerson);
        when(deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE)).thenReturn(availableDeliveryPersons);

        Delivery pendingDelivery = new Delivery();
        pendingDelivery.setStatus(DeliveryStatus.PENDING);
        pendingDelivery.setDestinationZipCode("12345");
        List<Delivery> pendingDeliveries = Collections.singletonList(pendingDelivery);
        when(deliveryRepository.findByStatus(DeliveryStatus.PENDING)).thenReturn(pendingDeliveries);

        SortedMap<String, List<Delivery>> routes = new TreeMap<>();
        routes.put("region 12345", pendingDeliveries);
        when(deliveryPersonService.calculateRoutesByRegion()).thenReturn(routes);

        deliveryPersonService.assignAvailableDeliveryPeopleToPendingDeliveries();

        assertEquals(DeliveryStatus.WAITING_FOR_PICKUP, pendingDelivery.getStatus());
        assertEquals(availablePerson, pendingDelivery.getDeliveryPerson());

        verify(deliveryPersonRepository, times(1)).findByStatus(DeliveryPersonStatus.AVAILABLE);
        verify(deliveryRepository, times(1)).findByStatus(DeliveryStatus.PENDING);
        verify(deliveryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testChangeDeliveryPersonStatus() {
        Long deliveryPersonId = 1L;
        ChangeDeliveryPersonStatusRequestDto requestDto = new ChangeDeliveryPersonStatusRequestDto(deliveryPersonId, DeliveryPersonStatus.UNAVAILABLE);

        DeliveryPerson deliveryPerson = new DeliveryPerson("John Doe", VehicleType.MOTORCYCLE);
        deliveryPerson.setId(deliveryPersonId);
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        when(deliveryPersonRepository.findById(deliveryPersonId)).thenReturn(Optional.of(deliveryPerson));
        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(deliveryPerson);

        DeliveryPersonResponseDto responseDto = deliveryPersonService.changeDeliveryPersonStatus(deliveryPersonId, requestDto);

        assertNotNull(responseDto);
        assertEquals(DeliveryPersonStatus.UNAVAILABLE, responseDto.getStatus());
        assertEquals(deliveryPersonId, responseDto.getId());

        verify(deliveryPersonRepository, times(1)).findById(deliveryPersonId);
        verify(deliveryPersonRepository, times(1)).save(any(DeliveryPerson.class));
    }

    @Test
    void testFindAllDeliveryPeople() {
        Pageable pageable = PageRequest.of(0, 10);
        DeliveryPerson deliveryPerson1 = new DeliveryPerson("John Doe", VehicleType.MOTORCYCLE);
        deliveryPerson1.setId(1L);
        DeliveryPerson deliveryPerson2 = new DeliveryPerson("Jane Doe", VehicleType.CAR);
        deliveryPerson2.setId(2L);

        Page<DeliveryPerson> page = new PageImpl<>(Arrays.asList(deliveryPerson1, deliveryPerson2), pageable, 2);
        when(deliveryPersonRepository.findAll(pageable)).thenReturn(page);

        Page<DeliveryPersonResponseDto> responseDtoPage = deliveryPersonService.findAllDeliveryPeople(pageable);

        assertEquals(2, responseDtoPage.getContent().size());
        assertEquals("John Doe", responseDtoPage.getContent().get(0).getNome());
        assertEquals("Jane Doe", responseDtoPage.getContent().get(1).getNome());

        verify(deliveryPersonRepository, times(1)).findAll(pageable);
    }

    @Test
    void testCalculateRoutesByRegionResponse() {

        UUID orderId = UUID.randomUUID();
        Delivery delivery1 = new Delivery();
        delivery1.setOrderId(orderId);
        delivery1.setStatus(DeliveryStatus.PENDING);
        delivery1.setDestinationZipCode("12345-020");
        delivery1.setLatitude(BigDecimal.valueOf(10.0));
        delivery1.setLongitude(BigDecimal.valueOf(20.0));

        DeliveryResponseDto deliveryResponseDto1 = new DeliveryResponseDto();
        deliveryResponseDto1.setOrderId(orderId);
        deliveryResponseDto1.setStatus(DeliveryStatus.PENDING);
        deliveryResponseDto1.setLatitude(BigDecimal.valueOf(10.0));
        deliveryResponseDto1.setLongitude(BigDecimal.valueOf(20.0));

        SortedMap<String, List<Delivery>> routes = new TreeMap<>();
        routes.put("region 12345", Arrays.asList(delivery1));
        when(deliveryPersonService.calculateRoutesByRegion()).thenReturn(routes);

        SortedMap<String, List<DeliveryResponseDto>> routesDto = deliveryPersonService.calculateRoutesByRegionResponse();

        assertEquals(1, routesDto.size());
        assertEquals(1, routesDto.get("region 12345").size());
        assertEquals(1L, routesDto.get("region 12345").get(0).getOrderId());

        verify(deliveryPersonService, times(1)).calculateRoutesByRegion();
    }
}
