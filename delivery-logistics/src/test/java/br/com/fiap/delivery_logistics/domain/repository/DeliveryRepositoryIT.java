package br.com.fiap.delivery_logistics.domain.repository;

import br.com.fiap.delivery_logistics.domain.model.Delivery;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPerson;
import br.com.fiap.delivery_logistics.domain.model.DeliveryStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.delivery_logistics.utils.DeliveryHelper.createDelivery;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class DeliveryRepositoryIT {

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    void shouldSaveDelivery() {
        // Arrange
        var delivery = createDelivery(DeliveryStatus.PENDING);

        // Act
        var savedDelivery = deliveryRepository.save(delivery);

        // Assert
        assertThat(savedDelivery).isNotNull();
        assertThat(savedDelivery.getOrderId()).isEqualTo(delivery.getOrderId());
        assertThat(savedDelivery.getStatus()).isEqualTo(delivery.getStatus());
    }

    @Test
    void shouldFindDeliveryByOrderId() {
        // Arrange
        var delivery = deliveryRepository.save(createDelivery(DeliveryStatus.PENDING));
        UUID orderId = delivery.getOrderId();

        // Act
        Optional<Delivery> foundDelivery = deliveryRepository.findByOrderId(orderId);

        // Assert
        assertThat(foundDelivery).isPresent();
        assertThat(foundDelivery.get()).isEqualTo(delivery);
    }

    @Test
    void shouldFindDeliveriesByStatus() {
        // Arrange
        var pendingDelivery1 = deliveryRepository.save(createDelivery(DeliveryStatus.PENDING));
        var pendingDelivery2 = deliveryRepository.save(createDelivery(DeliveryStatus.PENDING, UUID.fromString("916d6109-ec12-4220-b331-a7f06e62a4ef")));
        deliveryRepository.save(createDelivery(DeliveryStatus.DELIVERED, UUID.fromString("d4701c25-c961-4873-a7cc-e11686317c18")));

        // Act
        List<Delivery> foundDeliveries = deliveryRepository.findByStatus(DeliveryStatus.PENDING);

        // Assert
        assertThat(foundDeliveries).hasSize(3);
    }

    @Test
    void shouldDeleteDeliveryById() {
        // Arrange
        var delivery = deliveryRepository.save(createDelivery(DeliveryStatus.PENDING));
        UUID orderId = delivery.getOrderId();

        // Act
        deliveryRepository.deleteById(orderId);
        Optional<Delivery> deletedDelivery = deliveryRepository.findByOrderId(orderId);

        // Assert
        assertThat(deletedDelivery).isEmpty();
    }

    @Test
    void shouldFindAllDeliveries() {
        // Arrange
        var delivery1 = deliveryRepository.save(createDelivery(DeliveryStatus.PENDING));
        var delivery2 = deliveryRepository.save(createDelivery(DeliveryStatus.DELIVERED, UUID.fromString("d4701c25-c961-4873-a7cc-e11686317c18")));

        // Act
        List<Delivery> allDeliveries = deliveryRepository.findAll();

        // Assert
        assertThat(allDeliveries).hasSize(3);
    }
}
