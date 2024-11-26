package br.com.fiap.order_management.domain.output;

import br.com.fiap.order_management.domain.model.PaymentMethod;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class PaymentOutput {

    private UUID id;
    private PaymentMethod paymentMethod;
    private double value;

    private LocalDateTime paymentTimestamp;
    private String status;
    private String authNumber;

}
