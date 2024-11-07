package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.repository;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPerson;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryPersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    List<DeliveryPerson> findByStatus(DeliveryPersonStatus status);
}
