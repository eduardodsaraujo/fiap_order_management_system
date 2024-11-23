package br.com.fiap.order_management.domain.input;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.Data;

@Data
public class UpdatePaymentInput {

    private PaymentMethod paymentMethod;

}
