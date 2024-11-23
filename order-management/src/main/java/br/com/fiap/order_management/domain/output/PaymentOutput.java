package br.com.fiap.order_management.domain.output;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentOutput {

    private PaymentMethod paymentMethod;
    private double value;



}
