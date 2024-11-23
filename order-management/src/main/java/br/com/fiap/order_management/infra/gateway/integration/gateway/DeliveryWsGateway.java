package br.com.fiap.order_management.infra.gateway.integration.gateway;

import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.DeliveryRequestDto;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHeaders;

@MessagingGateway
public interface DeliveryWsGateway {

    @Gateway(requestChannel = "calculateShippingChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    CalculateShippingDto calculateShipping(CalculateShippingRequestDto entity);

    @Gateway(requestChannel = "createDeliveryChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    void createDelivery(DeliveryRequestDto entity);

}
