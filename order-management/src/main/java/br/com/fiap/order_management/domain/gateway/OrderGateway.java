package br.com.fiap.order_management.domain.gateway;

import br.com.fiap.order_management.domain.model.Order;

import java.util.List;
import java.util.UUID;

public interface OrderGateway {

    Order save(Order order);

    Order findById(UUID id);

    Order findByPaymentId(UUID paymentId);

    List<Order> findAllByCustomerId(long customerId);

}
