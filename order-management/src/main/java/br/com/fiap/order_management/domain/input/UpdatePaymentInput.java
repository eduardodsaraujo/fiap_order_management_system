package br.com.fiap.order_management.domain.input;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePaymentInput {

    private PaymentMethod paymentMethod;

}
