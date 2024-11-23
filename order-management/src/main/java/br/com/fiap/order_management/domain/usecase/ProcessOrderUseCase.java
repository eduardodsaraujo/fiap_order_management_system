package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.ProductGateway;
import br.com.fiap.order_management.domain.output.OrderOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessOrderUseCase {

    private final OrderGateway orderGateway;
    private final ProductGateway productGateway;
    private final DeliveryGateway deliveryGateway;

    public OrderOutput process(UUID orderId) {
        Order order = orderGateway.findById(orderId);

        // Process product stock
        for (OrderItem item : order.getItems()) {
            productGateway.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }
        order.process();

        try {
            // Process payment
            order.processPayment();

            // Process delivery
            deliveryGateway.createDelivery(order.getId(), order.getDeliveryAddress().getPostalCode());

            orderGateway.save(order);
        } catch (Exception e) {
            // Process product stock
            for (OrderItem item : order.getItems()) {
                productGateway.increaseStock(item.getProduct().getId(), item.getQuantity());
            }
        }

        return OrderOutputMapper.toOrderOutput(order);
    }

}
