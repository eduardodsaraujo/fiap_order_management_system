package br.com.fiap.order_management.domain.gateway;

import java.util.UUID;

public interface DeliveryGateway {

    double calculateShipping(String destinationZipCode, double weightProducts);

    void createDelivery(UUID orderId, String destinationZipCode);

}
