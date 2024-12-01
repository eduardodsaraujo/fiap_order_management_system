package br.com.fiap.delivery_logistics.domain.repository;

import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPerson;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.delivery_logistics.utils.DeliveryHelper.createDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeliveryRepositoryTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void shouldSaveDelivery() {
        // Arrange
        var delivery = createDelivery(DeliveryStatus.PENDING);
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(delivery);

        // Act
        var savedDelivery = deliveryRepository.save(delivery);

        // Assert
        assertThat(savedDelivery)
                .isNotNull()
                .isEqualTo(delivery);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));
    }

    @Test
    void shouldFindDeliveryByOrderId() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        var delivery = createDelivery(DeliveryStatus.PENDING);
        delivery.setOrderId(orderId);
        when(deliveryRepository.findByOrderId(any(UUID.class))).thenReturn(Optional.of(delivery));

        // Act
        var foundDelivery = deliveryRepository.findByOrderId(orderId);

        // Assert
        assertThat(foundDelivery)
                .isPresent()
                .containsSame(delivery);
        verify(deliveryRepository, times(1)).findByOrderId(any(UUID.class));
    }

    @Test
    void shouldFindDeliveriesByStatus() {
        // Arrange
        DeliveryStatus status = DeliveryStatus.PENDING;
        var delivery1 = createDelivery(status);
        var delivery2 = createDelivery(status);
        List<Delivery> deliveries = Arrays.asList(delivery1, delivery2);

        when(deliveryRepository.findByStatus(any(DeliveryStatus.class))).thenReturn(deliveries);

        // Act
        var foundDeliveries = deliveryRepository.findByStatus(status);

        // Assert
        assertThat(foundDeliveries)
                .hasSize(2)
                .containsExactlyInAnyOrder(delivery1, delivery2);
        verify(deliveryRepository, times(1)).findByStatus(any(DeliveryStatus.class));
    }

    @Test
    void shouldDeleteDeliveryById() {
        // Arrange
        UUID orderId = UUID.randomUUID();
        doNothing().when(deliveryRepository).deleteById(any(UUID.class));

        // Act
        deliveryRepository.deleteById(orderId);

        // Assert
        verify(deliveryRepository, times(1)).deleteById(any(UUID.class));
    }

}
