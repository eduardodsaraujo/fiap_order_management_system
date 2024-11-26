package br.com.fiap.order_management.infra.gateway.payment.mock;

import br.com.fiap.order_management.domain.gateway.PaymentGateway;
import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.PaymentReceipt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentMockGateway implements PaymentGateway {

    @Override
    public UUID requestPayment(Order order) {
        return UUID.randomUUID();
    }

    @Override
    public PaymentReceipt getPayment(UUID requestPaymentId) {
        return new PaymentReceipt(
                requestPaymentId,
                LocalDateTime.now(),
                "PAID",
                UUID.randomUUID().toString()
        );
    }

}
