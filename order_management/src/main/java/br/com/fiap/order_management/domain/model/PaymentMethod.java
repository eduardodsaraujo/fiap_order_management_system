package br.com.fiap.order_management.domain.model;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    CASH,
    BOLETO,
    CREDIT_CARD,
    DEBIT_CARD,
    PIX
}
