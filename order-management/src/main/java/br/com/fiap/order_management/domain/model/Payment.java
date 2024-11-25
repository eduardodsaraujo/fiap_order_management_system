package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class Payment {

    private UUID id;
    private PaymentMethod paymentMethod;
    private double value;
    private LocalDateTime paymentTimestamp;
    private String status;
    private String authNumber;

}
