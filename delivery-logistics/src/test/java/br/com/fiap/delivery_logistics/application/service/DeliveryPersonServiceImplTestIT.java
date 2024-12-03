package br.com.fiap.delivery_logistics.application.service;

import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.ChangeDeliveryPersonStatusRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonRequestDto;
import br.com.fiap.delivery_logistics.application.dto.deliveryPerson.DeliveryPersonResponseDto;
import br.com.fiap.delivery_logistics.application.service.impl.DeliveryPersonServiceImpl;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPerson;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.delivery_logistics.domain.model.VehicleType;
import br.com.fiap.delivery_logistics.domain.repository.DeliveryPersonRepository;
import br.com.fiap.delivery_logistics.infrastructure.exception.DeliveryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DeliveryPersonServiceImplTestIT {

    @Autowired
    private DeliveryPersonServiceImpl deliveryPersonService;

    @Autowired
    private DeliveryPersonRepository deliveryPersonRepository;

    @BeforeEach
    void setUp() {
        DeliveryPerson deliveryPerson = new DeliveryPerson();
        deliveryPerson.setName("Edy Delivery");
        deliveryPerson.setVehicleType(VehicleType.BICYCLE);
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);
        deliveryPersonRepository.save(deliveryPerson);
    }

    @Test
    void shouldReturnListOfDeliveryPeople() {
        var pageable = org.springframework.data.domain.PageRequest.of(0, 10);
        List<DeliveryPersonResponseDto> deliveryPeople = deliveryPersonService.findAllDeliveryPeople(pageable).getContent();

        assertNotNull(deliveryPeople);
        assertEquals(2, deliveryPeople.size());
        assertEquals("Edy Delivery", deliveryPeople.get(1).getName());
    }

    @Test
    void shouldReturnSavedDeliveryPersonDTO() {
        DeliveryPersonRequestDto requestDto = new DeliveryPersonRequestDto();
        requestDto.setName("Jane Delivery");
        requestDto.setVehicleType(VehicleType.CAR);

        DeliveryPersonResponseDto result = deliveryPersonService.createDeliveryPerson(requestDto);

        assertNotNull(result);
        assertEquals("Jane Delivery", result.getName());
        assertEquals(VehicleType.CAR, result.getVehicleType());
        assertEquals(DeliveryPersonStatus.AVAILABLE, result.getStatus());
    }

    @Test
    void shouldThrowException_WhenNoAvailableDeliveryPerson() {
        // Alterar todos os entregadores para "IN_DELIVERY"
        List<DeliveryPerson> deliveryPeople = deliveryPersonRepository.findAll();
        deliveryPeople.forEach(dp -> dp.setStatus(DeliveryPersonStatus.UNAVAILABLE));
        deliveryPersonRepository.saveAll(deliveryPeople);

        Exception exception = assertThrows(DeliveryException.class,
                () -> deliveryPersonService.assignAvailableDeliveryPeopleToPendingDeliveries());

        assertEquals("Not  available delivery persons.", exception.getMessage());
    }

    @Test
    void shouldChangeDeliveryPersonStatus() {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findAll().get(0);

        ChangeDeliveryPersonStatusRequestDto requestDto = new ChangeDeliveryPersonStatusRequestDto();
        requestDto.setStatus(DeliveryPersonStatus.UNAVAILABLE);

        DeliveryPersonResponseDto updatedDeliveryPerson =
                deliveryPersonService.changeDeliveryPersonStatus(deliveryPerson.getId(), requestDto);

        assertNotNull(updatedDeliveryPerson);
        assertEquals(DeliveryPersonStatus.UNAVAILABLE, updatedDeliveryPerson.getStatus());
    }

    @Test
    void shouldThrowException_WhenDeliveryPersonNotFound() {
        Exception exception = assertThrows(DeliveryException.class,
                () -> deliveryPersonService.changeDeliveryPersonStatus(999L, new ChangeDeliveryPersonStatusRequestDto()));

        assertEquals("Delivery Person not found", exception.getMessage());
    }
}
