package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.application.service.dto.DeliveryPersonRequestDto;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.Delivery;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPerson;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPersonStatus;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryStatus;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.repository.DeliveryPersonRepository;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.repository.DeliveryRepository;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.infrastructure.exception.DeliveryException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryRepository deliveryRepository;

    public void createDeliveryPerson(DeliveryPersonRequestDto deliveryPersonRequestDto) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(deliveryPersonRequestDto.getNome(),
                deliveryPersonRequestDto.getVehicleType());
        deliveryPerson.setStatus(DeliveryPersonStatus.AVAILABLE);
        deliveryPersonRepository.save(deliveryPerson);
    }

    public void assignAvailableDeliveryPersonsToPendingDeliveries() {
        // Buscar entregadores disponíveis
        List<DeliveryPerson> availableDeliveryPersons = deliveryPersonRepository.findByStatus(DeliveryPersonStatus.AVAILABLE);

        // Buscar entregas com status "PENDING"
        List<Delivery> pendingDeliveries = deliveryRepository.findByStatus(DeliveryStatus.PENDING);

        // Verifica se há entregadores disponíveis
        if (availableDeliveryPersons.size() == 0) {
            throw new DeliveryException("Not  available delivery persons.");
        }

        // Atribuir entregadores às entregas
        for (int i = 0; i < pendingDeliveries.size(); i++) {
            Delivery delivery = pendingDeliveries.get(i);

            DeliveryPerson deliveryPerson = availableDeliveryPersons.get(0);
            deliveryPerson.setStatus(DeliveryPersonStatus.UNAVAILABLE);

            delivery.setDeliveryPerson(deliveryPerson);
            delivery.setStatus(DeliveryStatus.WAITING_FOR_PICKUP);
            delivery.setLastUpdated(LocalDateTime.now());
        }

        // Salvar as atualizações no banco de dados
        deliveryRepository.saveAll(pendingDeliveries);

    }
}
