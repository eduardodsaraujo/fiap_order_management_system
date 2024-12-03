package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.delivery.ChangeDeliveryStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryRequestDto;
import br.com.fiap.delivery_logistics.application.dto.delivery.DeliveryResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.DeliveryServiceImpl;
import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryRepository;
import br.com.fiap.delivery_logistics.infrastructure.client.OrderManagementClient;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.delivery_logistics.utils.DeliveryHelper.createDelivery;
import static br.com.fiap.delivery_logistics.utils.DeliveryHelper.createDeliveryResponseDto;
import static br.com.fiap.delivery_logistics.utils.DeliveryPersonHelper.createDeliveryPerson;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeliveryServiceImplTest {

    private DeliveryServiceImpl deliveryService;

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private OrderManagementClient orderManagementClient;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        deliveryService = new DeliveryServiceImpl(deliveryRepository, orderManagementClient);
    }

    @Test
    void shouldCreateDelivery() {
        // Arrange
        DeliveryRequestDto requestDto = new DeliveryRequestDto();
        requestDto.setOrderId(UUID.randomUUID());
        requestDto.setDestinationZipCode("12345678");

        Delivery delivery = new Delivery();
        delivery.setOrderId(requestDto.getOrderId());
        delivery.setDestinationZipCode(requestDto.getDestinationZipCode());
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setLastUpdated(LocalDateTime.now());

        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        // Act
        DeliveryResponseDto response = deliveryService.createDelivery(requestDto);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getOrderId()).isEqualTo(requestDto.getOrderId());
        Assertions.assertThat(response.getStatus()).isEqualTo(DeliveryStatus.PENDING);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void shouldSaveDeliveryTrack() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        BigDecimal latitude = BigDecimal.valueOf(40.7128);
        BigDecimal longitude = BigDecimal.valueOf(-74.0060);

        Delivery delivery = new Delivery();
        var deliveryPerson = createDeliveryPerson(DeliveryPersonStatus.UNAVAILABLE);
        delivery.setDeliveryPerson(deliveryPerson);
        delivery.setOrderId(orderId);

        when(deliveryRepository.findByOrderId(orderId)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        // Act
        DeliveryResponseDto response = deliveryService.saveDeliveryTrack(orderId, latitude, longitude);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getOrderId()).isEqualTo(orderId);
        Assertions.assertThat(response.getLatitude()).isEqualTo(latitude);
        Assertions.assertThat(response.getLongitude()).isEqualTo(longitude);
        verify(deliveryRepository, times(1)).findByOrderId(orderId);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void shouldThrowExceptionWhenDeliveryNotFoundByOrderId() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        when(deliveryRepository.findByOrderId(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deliveryService.saveDeliveryTrack(orderId, BigDecimal.ZERO, BigDecimal.ZERO))
                .isInstanceOf(DeliveryException.class)
                .hasMessage("Delivery not found");
        verify(deliveryRepository, times(1)).findByOrderId(orderId);
    }

    @Test
    void shouldChangeDeliveryStatus() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        ChangeDeliveryStatusRequestDto requestDto = new ChangeDeliveryStatusRequestDto();
        requestDto.setStatus(DeliveryStatus.DELIVERED);

        Delivery delivery = new Delivery();
        delivery.setOrderId(orderId);
        delivery.setStatus(DeliveryStatus.PENDING);

        when(deliveryRepository.findById(orderId)).thenReturn(Optional.of(delivery));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        // Act
        DeliveryResponseDto response = deliveryService.changeDeliveryStatus(orderId, requestDto);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
        verify(orderManagementClient, times(1)).updateDelivered(orderId);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void shouldThrowExceptionWhenChangingStatusOfNonExistentDelivery() {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        ChangeDeliveryStatusRequestDto requestDto = new ChangeDeliveryStatusRequestDto();
        requestDto.setStatus(DeliveryStatus.DELIVERED);

        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deliveryService.changeDeliveryStatus(deliveryId, requestDto))
                .isInstanceOf(DeliveryException.class)
                .hasMessage("Delivery not found");
        verify(deliveryRepository, times(1)).findById(deliveryId);
    }

    @Test
    void shouldGetDeliveryById() {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        Delivery delivery = new Delivery();
        delivery.setOrderId(deliveryId);
        delivery.setStatus(DeliveryStatus.PENDING);
        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));

        // Act
        DeliveryResponseDto response = deliveryService.getById(deliveryId);

        // Assert
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getOrderId()).isEqualTo(deliveryId);
        Assertions.assertThat(response.getStatus()).isEqualTo(DeliveryStatus.PENDING);
        verify(deliveryRepository, times(1)).findById(deliveryId);
    }

    @Test
    void shouldThrowExceptionWhenDeliveryNotFoundById() {
        // Arrange
        UUID deliveryId = UUID.randomUUID();
        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> deliveryService.getById(deliveryId))
                .isInstanceOf(DeliveryException.class)
                .hasMessage("Delivery not found");
        verify(deliveryRepository, times(1)).findById(deliveryId);
    }

    @Test
    void shouldReturnAllDeliveriesPaged() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        var delivery = createDelivery(DeliveryStatus.PENDING);
        var delivery2 = createDelivery(DeliveryStatus.PENDING);

        List<Delivery> deliveryList = Arrays.asList(delivery, delivery2);
        Page<Delivery> deliveryPage =
                new PageImpl<>(deliveryList, pageable, deliveryList.size());


        when(deliveryRepository.findAll(pageable)).thenReturn(deliveryPage);

        // Act
        Page<DeliveryResponseDto> response = deliveryService.findAllDeliveries(pageable);

        // Assert
        Assertions.assertThat(response).isNotNull();
        verify(deliveryRepository, times(1)).findAll(pageable);
    }

}