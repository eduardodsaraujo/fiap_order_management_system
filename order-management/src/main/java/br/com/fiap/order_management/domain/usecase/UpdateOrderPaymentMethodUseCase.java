package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.input.UpdatePaymentInput;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.output.OrderOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateOrderPaymentMethodUseCase {

    private final OrderGateway orderGateway;

    public OrderOutput update(UUID orderId, UpdatePaymentInput input) {
        Order order = orderGateway.findById(orderId);

        Payment payment = Payment.builder()
                .paymentMethod(input.getPaymentMethod())
                .value(order.getTotal())
                .build();

        order.updatePayment(payment);

        order = orderGateway.save(order);

        return OrderOutputMapper.toOrderOutput(order);
    }

}
