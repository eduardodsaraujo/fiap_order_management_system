package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.output.OrderOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FindAllOrdersByCustomerIdUseCase {

    private final OrderGateway orderGateway;

    public List<OrderOutput> execute(long customerId) {
        List<Order> orders = orderGateway.findAllByCustomerId(customerId);

        return orders.stream().map(OrderOutputMapper::toOrderOutput).toList();
    }

}
