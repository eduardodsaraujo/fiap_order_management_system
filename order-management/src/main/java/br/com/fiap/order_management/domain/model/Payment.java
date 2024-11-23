package br.com.fiap.order_management.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class Payment {

    private UUID id;
    private PaymentMethod paymentMethod;
    private double value;

}
