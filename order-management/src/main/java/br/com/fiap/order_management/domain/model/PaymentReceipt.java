package br.com.fiap.order_management.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PaymentReceipt {

    private UUID requesteId;
    private LocalDateTime paymentTimestamp;
    private String status;
    private String authNumber;
}
