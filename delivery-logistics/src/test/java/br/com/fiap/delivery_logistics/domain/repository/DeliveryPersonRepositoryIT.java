package br.com.fiap.delivery_logistics.domain.repository;

import br.com.fiap.delivery_logistics.domain.model.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static br.com.fiap.delivery_logistics.utils.DeliveryPersonHelper.createDeliveryPerson;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
class DeliveryPersonRepositoryIT {

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;


    @Test
    void shouldCreateTable() {
        // Act
        long registers = deliveryPersonRepository.count();
        // Assert
        assertThat(registers).isNotNull();
    }

    @Test
    void shouldSaveDeliveryPerson() {
        // Arrange
        var deliveryPerson = createDeliveryPerson(DeliveryPersonStatus.AVAILABLE);

        // Act
        var savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);

        // Assert
        assertThat(savedDeliveryPerson)
                .isNotNull()
                .isInstanceOf(DeliveryPerson.class);
        assertThat(savedDeliveryPerson.getName()).isEqualTo(deliveryPerson.getName());
        assertThat(savedDeliveryPerson.getStatus()).isEqualTo(deliveryPerson.getStatus());
    }

    @Test
    void shouldFindDeliveryPersonById() {
        // Arrange
        var deliveryPerson = createDeliveryPerson(DeliveryPersonStatus.AVAILABLE);
        var savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        var id = savedDeliveryPerson.getId();

        // Act
        var foundDeliveryPersonOptional = deliveryPersonRepository.findById(id);

        // Assert
        assertThat(foundDeliveryPersonOptional).isPresent().containsSame(savedDeliveryPerson);
    }

    @Test
    void shouldDeleteDeliveryPersonById() {
        // Arrange
        var deliveryPerson = createDeliveryPerson(DeliveryPersonStatus.AVAILABLE);
        var savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);
        var id = savedDeliveryPerson.getId();

        // Act
        deliveryPersonRepository.deleteById(id);
        var foundDeliveryPersonOptional = deliveryPersonRepository.findById(id);

        // Assert
        assertThat(foundDeliveryPersonOptional).isEmpty();
    }

    @Test
    void shouldFindAllDeliveryPersons() {
        // Arrange
        var deliveryPerson1 = createDeliveryPerson(DeliveryPersonStatus.AVAILABLE);

        var deliveryPerson2 = createDeliveryPerson(DeliveryPersonStatus.UNAVAILABLE);

        deliveryPersonRepository.save(deliveryPerson1);
        deliveryPersonRepository.save(deliveryPerson2);

        // Act
        List<DeliveryPerson> deliveryPersons = deliveryPersonRepository.findAll();

        // Assert
        assertThat(deliveryPersons).hasSize(2);
    }

    @Test
    void shouldFindDeliveryPersonsByStatus() {
        // Arrange
        var deliveryPerson1 = createDeliveryPerson(DeliveryPersonStatus.AVAILABLE);

        var deliveryPerson2 = createDeliveryPerson(DeliveryPersonStatus.UNAVAILABLE);

        deliveryPersonRepository.save(deliveryPerson1);
        deliveryPersonRepository.save(deliveryPerson2);

        // Act
        List<DeliveryPerson> availablePersons = deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE);

        // Assert
        assertThat(availablePersons)
                .hasSize(1)
                .extracting(DeliveryPerson::getName)
                .containsExactly("Juninho");
    }
}
