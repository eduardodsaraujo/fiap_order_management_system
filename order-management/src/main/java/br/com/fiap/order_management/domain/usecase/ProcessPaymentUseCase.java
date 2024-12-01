package br.com.fiap.order_management.domain.usecase;

import br.com.fiap.order_management.domain.gateway.*;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.OrderItem;
import br.com.fiap.order_management.domain.model.PaymentReceipt;
import br.com.fiap.order_management.infra.exception.OrderException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final OrderGateway orderGateway;
    private final PaymentGateway paymentGateway;
    private final DeliveryGateway deliveryGateway;
    private final StockProductGateway stockProductGateway;

    @Transactional
    public void execute(UUID requestPaymentId) throws Exception {
        Order order = orderGateway.findByPaymentId(requestPaymentId);

        if (order.getPayment().getStatus() != null) {
            throw new OrderException("Payment already processed.");
        }

        PaymentReceipt paymentReceipt = paymentGateway.getPayment(requestPaymentId);
        order.processPayment(paymentReceipt);

        if (paymentReceipt.getStatus().equals("PAID")) {
            deliveryGateway.createDelivery(order.getId(), order.getDeliveryAddress().getPostalCode());
            order.updateAwaitingShipment();
        } else {
            order.cancel();

            for (OrderItem item : order.getItems()) {
                stockProductGateway.increaseStock(item.getProduct().getId(), item.getQuantity());
            }
        }

        orderGateway.save(order);
    }

}
