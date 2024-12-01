package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.output.OrderOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FindOrderByIdUseCase {

    private final OrderGateway orderGateway;

    public OrderOutput execute(UUID orderId) {
        Order order = orderGateway.findById(orderId);

        return OrderOutputMapper.toOrderOutput(order);
    }

}
