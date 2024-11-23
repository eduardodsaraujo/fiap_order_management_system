package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.gateway.DeliveryGateway;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.DeliveryRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.DeliveryWsGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeliveryIntegrationGateway implements DeliveryGateway {

    private final DeliveryWsGateway deliveryWsGateway;

    @Override
    public double calculateShipping(String destinationZipCode, double weightProducts) {
        CalculateShippingRequestDto requestDto = new CalculateShippingRequestDto(destinationZipCode, weightProducts);
        CalculateShippingDto calculateShippingDto = deliveryWsGateway.calculateShipping(requestDto);

        return calculateShippingDto.getCost();
    }

    @Override
    public void createDelivery(UUID orderId, String destinationZipCode) {
        DeliveryRequestDto requestDto = new DeliveryRequestDto(orderId,destinationZipCode);
        deliveryWsGateway.createDelivery(requestDto);
    }

}
