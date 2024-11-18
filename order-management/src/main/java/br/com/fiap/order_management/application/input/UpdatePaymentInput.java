package br.com.fiap.order_management.application.input;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.Getter;

@Getter
public class UpdatePaymentInput {

    private PaymentMethod paymentMethod;

}
