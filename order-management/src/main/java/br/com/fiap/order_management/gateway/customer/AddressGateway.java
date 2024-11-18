package br.com.fiap.order_management.gateway.customer;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHeaders;

@MessagingGateway
public interface AddressGateway {

    @Gateway(requestChannel = "addressChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    AddressEntity findByCustomerIdAndAddressId(Long... params);


}
