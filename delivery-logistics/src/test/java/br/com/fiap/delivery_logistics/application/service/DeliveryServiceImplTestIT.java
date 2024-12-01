package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.DeliveryServiceImpl;
import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryRepository;
import br.com.fiap.delivery_logistics.infrastructure.client.OrderManagementClient;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class DeliveryServiceImplTestIT {

    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @MockBean
    private OrderManagementClient orderManagementClient; // Mockando o client

    private UUID orderId;

    @BeforeEach
    void setUp() {
        Delivery delivery = new Delivery();
        delivery.setOrderId(UUID.randomUUID());
        delivery.setDestinationZipCode("12345678");
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setLastUpdated(LocalDateTime.now());
        Delivery savedDelivery = deliveryRepository.save(delivery);
        this.orderId = savedDelivery.getOrderId();
    }

    @Test
    void shouldCreateDelivery() {
        DeliveryRequestDto requestDto = new DeliveryRequestDto();
        requestDto.setOrderId(UUID.randomUUID());
        requestDto.setDestinationZipCode("98765432");

        DeliveryResponseDto responseDto = deliveryService.createDelivery(requestDto);

        assertNotNull(responseDto);
        assertEquals(requestDto.getOrderId(), responseDto.getOrderId());
        assertEquals(DeliveryStatus.PENDING, responseDto.getStatus());
    }

    @Test
    void shouldSaveDeliveryTrack() {
        BigDecimal latitude = BigDecimal.valueOf(10.1234);
        BigDecimal longitude = BigDecimal.valueOf(20.5678);

        DeliveryResponseDto responseDto = deliveryService.saveDeliveryTrack(orderId, latitude, longitude);

        assertNotNull(responseDto);
        assertEquals(latitude, responseDto.getLatitude());
        assertEquals(longitude, responseDto.getLongitude());
    }

    @Test
    void shouldThrowExceptionWhenDeliveryNotFoundForTracking() {
        UUID invalidOrderId = UUID.randomUUID();
        Exception exception = assertThrows(DeliveryException.class,
                () -> deliveryService.saveDeliveryTrack(invalidOrderId, BigDecimal.ZERO, BigDecimal.ZERO));

        assertEquals("Delivery not found", exception.getMessage());
    }

    @Test
    void shouldGetDeliveryById() {
        Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow();

        DeliveryResponseDto responseDto = deliveryService.getById(delivery.getOrderId());

        assertNotNull(responseDto);
        assertEquals(delivery.getOrderId(), responseDto.getOrderId());
        assertEquals(delivery.getStatus(), responseDto.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenDeliveryNotFoundById() {
        UUID invalidId = UUID.randomUUID();
        Exception exception = assertThrows(DeliveryException.class,
                () -> deliveryService.getById(invalidId));

        assertEquals("Delivery not found", exception.getMessage());
    }

    @Test
    void shouldChangeDeliveryStatus() {
        ChangeDeliveryStatusRequestDto requestDto = new ChangeDeliveryStatusRequestDto();
        requestDto.setStatus(DeliveryStatus.DELIVERED);

        when(orderManagementClient.updateDelivered(requestDto.getDeliveryId())).thenReturn(ResponseEntity.ok().build());

        Delivery delivery = deliveryRepository.findByOrderId(orderId).orElseThrow();

        DeliveryResponseDto responseDto = deliveryService.changeDeliveryStatus(delivery.getOrderId(), requestDto);

        assertNotNull(responseDto);
        assertEquals(DeliveryStatus.DELIVERED, responseDto.getStatus());
    }

    @Test
    void shouldFindAllDeliveries() {
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        var deliveries = deliveryService.findAllDeliveries(pageable);

        assertNotNull(deliveries);
        assertFalse(deliveries.isEmpty());
    }
}
