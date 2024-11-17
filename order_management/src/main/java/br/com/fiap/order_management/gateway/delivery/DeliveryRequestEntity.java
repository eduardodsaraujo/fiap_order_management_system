package br.com.fiap.order_management.gateway.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DeliveryRequestEntity {

    private UUID orderId;
    private String destinationZipCode;

}
