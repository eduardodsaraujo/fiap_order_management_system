package br.com.fiap.order_management.gateway.delivery;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHeaders;

@MessagingGateway
public interface DeliveryGateway {

    @Gateway(requestChannel = "calculateShippingChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    CalculateShippingResponseEntity calculateShipping(CalculateShippingRequestEntity entity);

    @Gateway(requestChannel = "createDeliveryChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    void createDelivery(DeliveryRequestEntity entity);

}
