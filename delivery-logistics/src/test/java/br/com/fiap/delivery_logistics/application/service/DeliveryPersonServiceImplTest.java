package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.DeliveryPersonServiceImpl;
import br.com.fiap.delivery_logistics.domain.model.*;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryPersonRepository;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryRepository;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeliveryPersonServiceImplTest {

    private DeliveryPersonServiceImpl deliveryPersonService;

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @Mock
    private DeliveryRepository deliveryRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        deliveryPersonService = new DeliveryPersonServiceImpl(deliveryPersonRepository, deliveryRepository);
    }

    @Test
    void shouldCreateDeliveryPerson() {
        // Arrange
        var request = new DeliveryPersonRequestDto();
        request.setName("John Doe");
        request.setVehicleType(VehicleType.BICYCLE);

        var deliveryPerson = new DeliveryPerson("John Doe", VehicleType.BICYCLE);
        deliveryPerson.setId(1L);
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(deliveryPerson);

        // Act
        DeliveryPersonResponseDto response = deliveryPersonService.createDeliveryPerson(request);

        // Assert
        assertThat(response)
                .isNotNull()
                .extracting(DeliveryPersonResponseDto::getId, DeliveryPersonResponseDto::getName, DeliveryPersonResponseDto::getStatus, DeliveryPersonResponseDto::getVehicleType)
                .containsExactly(1L, "John Doe", DeliveryPersonStatus.AVAILABLE, VehicleType.BICYCLE);
        verify(deliveryPersonRepository, times(1)).save(any(DeliveryPerson.class));
    }

    @Test
    void shouldAssignAvailableDeliveryPeopleToPendingDeliveries() {
        // Arrange
        var deliveryPerson = new DeliveryPerson("Jane Smith", VehicleType.CAR);
        deliveryPerson.setId(1L);
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        var delivery = new Delivery();
        delivery.setOrderId(UUID.randomUUID());
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setDestinationZipCode("12345678");

        when(deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE))
                .thenReturn(List.of(deliveryPerson));
        when(deliveryRepository.findByStatus(DeliveryStatus.PENDING))
                .thenReturn(List.of(delivery));

        // Act
        deliveryPersonService.assignAvailableDeliveryPeopleToPendingDeliveries();

        // Assert
        verify(deliveryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void shouldThrowExceptionWhenNoAvailableDeliveryPersons() {
        // Arrange
        when(deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE)).thenReturn(new ArrayList<>());

        // Act & Assert
        assertThatThrownBy(() -> deliveryPersonService.assignAvailableDeliveryPeopleToPendingDeliveries())
                .isInstanceOf(DeliveryException.class)
                .hasMessage("Not  available delivery persons.");
    }

    @Test
    void shouldChangeDeliveryPersonStatus() {
        // Arrange
        var deliveryPerson = new DeliveryPerson("John Doe", VehicleType.BICYCLE);
        deliveryPerson.setId(1L);
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        var request = new ChangeDeliveryPersonStatusRequestDto();
        request.setStatus(DeliveryPersonStatus.UNAVAILABLE);

        when(deliveryPersonRepository.findById(1L)).thenReturn(Optional.of(deliveryPerson));
        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(deliveryPerson);

        // Act
        var response = deliveryPersonService.changeDeliveryPersonStatus(1L, request);

        // Assert
        assertThat(response)
                .isNotNull()
                .extracting(DeliveryPersonResponseDto::getId, DeliveryPersonResponseDto::getStatus)
                .containsExactly(1L, DeliveryPersonStatus.UNAVAILABLE);
        verify(deliveryPersonRepository, times(1)).save(any(DeliveryPerson.class));
    }

    @Test
    void shouldThrowExceptionWhenDeliveryPersonNotFound() {
        // Arrange
        when(deliveryPersonRepository.findById(1L)).thenReturn(Optional.empty());

        var request = new ChangeDeliveryPersonStatusRequestDto();
        request.setStatus(DeliveryPersonStatus.UNAVAILABLE);

        // Act & Assert
        assertThatThrownBy(() -> deliveryPersonService.changeDeliveryPersonStatus(1L, request))
                .isInstanceOf(DeliveryException.class)
                .hasMessage("Delivery Person not found");
    }

    @Test
    void shouldCalculateRoutesByRegion() {
        Delivery delivery1 = new Delivery();
        delivery1.setOrderId(UUID.randomUUID());
        delivery1.setStatus(DeliveryStatus.PENDING);
        delivery1.setDestinationZipCode("12345678");

        Delivery delivery2 = new Delivery();
        delivery2.setOrderId(UUID.randomUUID());
        delivery2.setStatus(DeliveryStatus.PENDING);
        delivery2.setDestinationZipCode("12345999");

        Delivery delivery3 = new Delivery();
        delivery3.setOrderId(UUID.randomUUID());
        delivery3.setStatus(DeliveryStatus.PENDING);
        delivery3.setDestinationZipCode("98765432");

        List<Delivery> pendingDeliveries = Arrays.asList(delivery1, delivery2, delivery3);

        when(deliveryRepository.findByStatus(DeliveryStatus.PENDING)).thenReturn(pendingDeliveries);

        // Act
        SortedMap<String, List<Delivery>> routesByRegion = deliveryPersonService.calculateRoutesByRegion();

        // Assert
        assertThat(routesByRegion).isNotNull();
        assertThat(routesByRegion).hasSize(2);
        assertThat(routesByRegion.get("region 12345")).containsExactlyInAnyOrder(delivery1, delivery2);
        assertThat(routesByRegion.get("region 98765")).containsExactly(delivery3);

        verify(deliveryRepository, times(1)).findByStatus(DeliveryStatus.PENDING);
    }

}
