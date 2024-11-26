package br.com.fiap.order_management.domain.gateway;

import br.com.fiap.order_management.domain.model.Order;
import br.com.fiap.order_management.domain.model.PaymentReceipt;

import java.util.UUID;

public interface PaymentGateway {

    UUID requestPayment(Order order);

    PaymentReceipt getPayment(UUID requestPaymentId);

}
