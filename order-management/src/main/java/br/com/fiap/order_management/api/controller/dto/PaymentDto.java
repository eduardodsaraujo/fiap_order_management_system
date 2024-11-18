package br.com.fiap.order_management.api.controller.dto;

import br.com.fiap.order_management.domain.model.Payment;
import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentDto {

    private PaymentMethod paymentMethod;
    private double value;

    public static PaymentDto toDto(Payment payment) {
        return PaymentDto.builder()
                .paymentMethod(payment.getPaymentMethod())
                .value(payment.getValue())
                .build();
    }

}
