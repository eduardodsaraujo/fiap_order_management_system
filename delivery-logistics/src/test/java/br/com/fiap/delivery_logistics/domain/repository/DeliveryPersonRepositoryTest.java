package br.com.fiap.delivery_logistics.domain.repository;

import br.com.fiap.delivery_logistics.domain.model.DeliveryPerson;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeliveryPersonRepositoryTest {

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    AutoCloseable openMocks;

    @BeforeEach
    void setup(){
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception{
        openMocks.close();
    }


    @Test
    void shouldSaveDeliveryPerson(){
        // Arrange
        var deliveryPerson = new DeliveryPerson();
        deliveryPerson.setId(1L);
        deliveryPerson.setName("John Doe");
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        when(deliveryPersonRepository.save(any(DeliveryPerson.class))).thenReturn(deliveryPerson);

        // Act
        var savedDeliveryPerson = deliveryPersonRepository.save(deliveryPerson);

        // Assert
        assertThat(savedDeliveryPerson)
                .isNotNull()
                .isEqualTo(deliveryPerson);
        verify(deliveryPersonRepository, times(1)).save(any(DeliveryPerson.class));
    }

    @Test
    void shouldFindDeliveryPersonById(){
        // Arrange
        Long id = 1L;
        var deliveryPerson = new DeliveryPerson();
        deliveryPerson.setId(id);
        deliveryPerson.setName("John Doe");
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        when(deliveryPersonRepository.findById(any(Long.class))).thenReturn(Optional.of(deliveryPerson));

        // Act
        var foundDeliveryPersonOptional = deliveryPersonRepository.findById(id);

        // Assert
        assertThat(foundDeliveryPersonOptional).isPresent().containsSame(deliveryPerson);
        foundDeliveryPersonOptional.ifPresent(found -> {
            assertThat(found.getId()).isEqualTo(deliveryPerson.getId());
            assertThat(found.getName()).isEqualTo(deliveryPerson.getName());
            assertThat(found.getStatus()).isEqualTo(deliveryPerson.getStatus());
        });
        verify(deliveryPersonRepository, times(1)).findById(any(Long.class));
    }

    @Test
    void shouldDeleteDeliveryPersonById(){
        // Arrange
        Long id = 1L;
        doNothing().when(deliveryPersonRepository).deleteById(any(Long.class));

        // Act
        deliveryPersonRepository.deleteById(id);

        // Assert
        verify(deliveryPersonRepository, times(1)).deleteById(id);
    }

    @Test
    void shouldFindAllDeliveryPersons(){
        // Arrange
        var deliveryPerson1 = new DeliveryPerson();
        deliveryPerson1.setId(1L);
        deliveryPerson1.setName("John Doe");
        deliveryPerson1.setStatus(DeliveryPersonStatus.AVAILABLE);

        var deliveryPerson2 = new DeliveryPerson();
        deliveryPerson2.setId(2L);
        deliveryPerson2.setName("Jane Doe");
        deliveryPerson2.setStatus(DeliveryPersonStatus.UNAVAILABLE);

        var listDeliveryPersons = Arrays.asList(deliveryPerson1, deliveryPerson2);
        when(deliveryPersonRepository.findAll()).thenReturn(listDeliveryPersons);

        // Act
        var foundDeliveryPersons = deliveryPersonRepository.findAll();

        // Assert
        assertThat(foundDeliveryPersons)
                .hasSize(2)
                .containsExactlyInAnyOrder(deliveryPerson1, deliveryPerson2);
        verify(deliveryPersonRepository, times(1)).findAll();
    }

    @Test
    void shouldFindDeliveryPersonsByStatus(){
        // Arrange
        var availablePerson = new DeliveryPerson();
        availablePerson.setId(1L);
        availablePerson.setName("John Doe");
        availablePerson.setStatus(DeliveryPersonStatus.AVAILABLE);

        var busyPerson = new DeliveryPerson();
        busyPerson.setId(2L);
        busyPerson.setName("Jane Doe");
        busyPerson.setStatus(DeliveryPersonStatus.UNAVAILABLE);

        var deliveryPersons = Arrays.asList(availablePerson);

        when(deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE)).thenReturn(deliveryPersons);

        // Act
        var foundDeliveryPersons = deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE);

        // Assert
        assertThat(foundDeliveryPersons)
                .hasSize(1)
                .containsExactlyInAnyOrder(availablePerson);
        verify(deliveryPersonRepository, times(1)).findByStatus(DeliveryPersonStatus.AVAILABLE);
    }
}
