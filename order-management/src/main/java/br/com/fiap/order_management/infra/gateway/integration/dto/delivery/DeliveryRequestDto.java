package br.com.fiap.order_management.infra.gateway.integration.dto.delivery;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DeliveryRequestDto {

    private UUID orderId;
    private String destinationZipCode;

}
