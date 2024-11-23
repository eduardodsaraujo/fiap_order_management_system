package br.com.fiap.order_management.infra.gateway.database.mongo;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.infra.exception.OrderException;
import br.com.fiap.order_management.infra.gateway.database.mongo.document.OrderDocument;
import br.com.fiap.order_management.infra.gateway.database.mongo.mapper.OrderDocumentMapper;
import br.com.fiap.order_management.infra.gateway.database.mongo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderRepositoryGateway implements OrderGateway {

    private final OrderRepository orderRepository;

    @Override
    public Order save(Order order) {
        OrderDocument orderDocument = OrderDocumentMapper.toDocument(order);

        orderDocument = orderRepository.save(orderDocument);

        return OrderDocumentMapper.toDomain(orderDocument);
    }

    @Override
    public Order findById(UUID id) {
        OrderDocument orderDocument = orderRepository.findById(id)
                .orElseThrow(() -> new OrderException("Order not found"));

        return OrderDocumentMapper.toDomain(orderDocument);
    }

    @Override
    public List<Order> findAllByCustomerId(long customerId) {
        List<OrderDocument> ordersDocuments = orderRepository.findAllByCustomerId(customerId);

        return ordersDocuments.stream().map(OrderDocumentMapper::toDomain).toList();
    }

}
