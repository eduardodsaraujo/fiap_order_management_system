package br.com.fiap.order_management.domain.input;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInput {

    private PaymentMethod paymentMethod;

}
