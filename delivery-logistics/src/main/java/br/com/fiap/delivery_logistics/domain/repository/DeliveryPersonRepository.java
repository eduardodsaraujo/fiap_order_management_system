package br.com.fiap.delivery_logistics.domain.repository;

import br.com.fiap.delivery_logistics.domain.model.DeliveryPerson;
import br.com.fiap.delivery_logistics.domain.model.DeliveryPersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryPersonRepository extends JpaRepository<DeliveryPerson, Long> {
    List<DeliveryPerson> findByStatus(DeliveryPersonStatus status);

}
