package br.com.fiap.order_management.infra.gateway.integration.gateway;

import br.com.fiap.order_management.infra.gateway.integration.dto.customer.AddressDto;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHeaders;

@MessagingGateway
public interface AddressWsGateway {

    @Gateway(requestChannel = "addressChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    AddressDto findByCustomerIdAndAddressId(Long... params);


}
