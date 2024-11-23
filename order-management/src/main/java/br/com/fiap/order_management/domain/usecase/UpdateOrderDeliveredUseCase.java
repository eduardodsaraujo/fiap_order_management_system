package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class UpdateOrderDeliveredUseCase {

    private final OrderGateway orderGateway;

    @Transactional
    public void execute(UUID orderId) {
        Order order = orderGateway.findById(orderId);

        order.updateDelivered();

        orderGateway.save(order);
    }

}
