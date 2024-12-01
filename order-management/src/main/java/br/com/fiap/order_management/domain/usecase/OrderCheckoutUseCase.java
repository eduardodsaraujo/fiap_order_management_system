package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.OrderGateway;
import br.com.fiap.order_management.domain.gateway.PaymentGateway;
import br.com.fiap.order_management.domain.gateway.StockProductGateway;
import br.com.fiap.order_management.domain.input.PaymentInput;
import br.com.fiap.order_management.domain.mapper.OrderOutputMapper;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.domain.output.OrderOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderCheckoutUseCase {

    private final OrderGateway orderGateway;
    private final PaymentGateway paymentGateway;
    private final StockProductGateway stockProductGateway;

    @Transactional
    public OrderOutput execute(UUID orderId, PaymentInput input) {
        Order order = orderGateway.findById(orderId);
        order.validateIsOpen();
        order.validateDeliveryAddress();

        // Process product stock
        for (OrderItem item : order.getItems()) {
            stockProductGateway.decreaseStock(item.getProduct().getId(), item.getQuantity());
        }

        // Request payment
        UUID requestPaymentId = paymentGateway.requestPayment(order);
        Payment payment = Payment.builder()
                .id(requestPaymentId)
                .paymentMethod(input.getPaymentMethod())
                .value(order.getTotal())
                .build();
        order.updatePayment(payment);

        order = orderGateway.save(order);

        return OrderOutputMapper.toOrderOutput(order);
    }

}
