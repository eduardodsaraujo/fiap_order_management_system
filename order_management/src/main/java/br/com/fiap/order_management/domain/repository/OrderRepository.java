package br.com.fiap.order_management.domain.repository;

import br.com.fiap.order_management.domain.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface OrderRepository extends MongoRepository<Order, UUID> {
}
