package br.com.fiap.order_management.infra.gateway.database.mongo.mapper;

import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.infra.gateway.database.mongo.document.PaymentDocument;

public class PaymentDocumentMapper {

    public static Payment toDomain(PaymentDocument paymentDocument) {
        if (paymentDocument != null) {
            return Payment.builder()
                    .id(paymentDocument.getId())
                    .paymentMethod(paymentDocument.getPaymentMethod())
                    .value(paymentDocument.getValue())
                    .paymentTimestamp(paymentDocument.getPaymentTimestamp())
                    .status(paymentDocument.getStatus())
                    .authNumber(paymentDocument.getAuthNumber())
                    .build();
        }
        return null;
    }

    public static PaymentDocument toDocument(Payment payment) {
        if (payment != null) {
            return PaymentDocument.builder()
                    .id(payment.getId())
                    .paymentMethod(payment.getPaymentMethod())
                    .value(payment.getValue())
                    .paymentTimestamp(payment.getPaymentTimestamp())
                    .status(payment.getStatus())
                    .authNumber(payment.getAuthNumber())
                    .build();
        }
        return null;
    }

}
