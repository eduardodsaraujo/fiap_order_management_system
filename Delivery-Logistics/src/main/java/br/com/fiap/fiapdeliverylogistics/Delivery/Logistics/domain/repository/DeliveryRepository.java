package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.repository;

import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.Delivery;
import br.com.fiap.fiapdeliverylogistics.Delivery.Logistics.domain.model.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByOrderId(Long orderId);

    List<Delivery> findByStatus(DeliveryStatus status);
}
