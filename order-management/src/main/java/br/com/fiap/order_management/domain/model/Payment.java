package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Payment {

    private UUID id;
    private PaymentMethod paymentMethod;
    private double value;
    private PaymentReceipt receipt;

}
