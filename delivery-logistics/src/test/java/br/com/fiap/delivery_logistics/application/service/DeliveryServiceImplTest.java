package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.DeliveryServiceImpl;
import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.infrastructure.client.OrderManagementClient;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryRepository;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DeliveryServiceImplTest {

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderManagementClient orderManagementClient;

    private UUID orderId;
    private DeliveryRequestDto deliveryRequestDto;
    private ChangeDeliveryStatusRequestDto changeDeliveryStatusRequestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        orderId = UUID.randomUUID();
        deliveryRequestDto = new DeliveryRequestDto(orderId, "12345");
        changeDeliveryStatusRequestDto = new ChangeDeliveryStatusRequestDto();
        changeDeliveryStatusRequestDto.setStatus(DeliveryStatus.DELIVERED);
    }

    @Test
    void testCreateDelivery() {
        // Given
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setDestinationZipCode("12345");
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setLastUpdated(LocalDateTime.now());

        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        DeliveryResponseDto response = deliveryService.createDelivery(deliveryRequestDto);

        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(DeliveryStatus.PENDING, response.getStatus());
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void testGetById() {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setDestinationZipCode("12345");
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setLastUpdated(LocalDateTime.now());

        when(deliveryRepository.findById(orderId)).thenReturn(Optional.of(delivery));

        DeliveryResponseDto response = deliveryService.getById(orderId);

        assertNotNull(response);
        assertEquals(orderId, response.getOrderId());
        assertEquals(DeliveryStatus.PENDING, response.getStatus());
        verify(deliveryRepository, times(1)).findById(orderId);
    }

    @Test
    void testGetById_NotFound() {
        when(deliveryRepository.findById(orderId)).thenReturn(Optional.empty());
        DeliveryException exception = assertThrows(DeliveryException.class, () -> {
            deliveryService.getById(orderId);
        });

        assertEquals("Delivery not found", exception.getMessage());
    }

    @Test
    void testChangeDeliveryStatus() {
        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setStatus(DeliveryStatus.PENDING);

        when(deliveryRepository.findById(orderId)).thenReturn(Optional.of(delivery));
        doNothing().when(orderManagementClient).updateDelivered(orderId);
        DeliveryResponseDto response = deliveryService.changeDeliveryStatus(orderId, changeDeliveryStatusRequestDto);
        assertNotNull(response);
        assertEquals(DeliveryStatus.DELIVERED, response.getStatus());
        verify(deliveryRepository, times(1)).save(delivery);
        verify(orderManagementClient, times(1)).updateDelivered(orderId);
    }

    @Test
    void testChangeDeliveryStatus_NotFound() {
        when(deliveryRepository.findById(orderId)).thenReturn(Optional.empty());
        DeliveryException exception = assertThrows(DeliveryException.class, () -> {
            deliveryService.changeDeliveryStatus(orderId, changeDeliveryStatusRequestDto);
        });

        assertEquals("Delivery not found", exception.getMessage());
    }

    @Test
    void testSaveDeliveryTrack() {
        BigDecimal latitude = BigDecimal.valueOf(40.7128);
        BigDecimal longitude = BigDecimal.valueOf(-74.0060);

        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setLatitude(latitude);
        delivery.setLongitude(longitude);

        when(deliveryRepository.findByOrderId(orderId)).thenReturn(Optional.of(delivery));
        DeliveryResponseDto response = deliveryService.saveDeliveryTrack(orderId, latitude, longitude);
        assertNotNull(response);
        assertEquals(latitude, response.getLatitude());
        assertEquals(longitude, response.getLongitude());
        verify(deliveryRepository, times(1)).save(delivery);
    }
}
