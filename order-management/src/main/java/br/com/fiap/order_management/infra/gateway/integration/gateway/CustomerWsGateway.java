package br.com.fiap.order_management.infra.gateway.integration.gateway;

import br.com.fiap.order_management.infra.gateway.integration.dto.customer.CustomerDto;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHeaders;

@MessagingGateway
public interface CustomerWsGateway {

    @Gateway(requestChannel = "customerChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    CustomerDto findById(long customerId);

}
