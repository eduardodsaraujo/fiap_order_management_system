package br.com.fiap.order_management.infra.gateway.database.mongo.repository;

import br.com.fiap.order_management.infra.gateway.database.mongo.document.OrderDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends MongoRepository<OrderDocument, UUID> {

    List<OrderDocument> findAllByCustomerId(long customerId);

}
