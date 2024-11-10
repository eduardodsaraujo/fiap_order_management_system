package br.com.fiap.order_management.infra.config;

import br.com.fiap.order_management.application.gateway.customer.AddressEntity;
import br.com.fiap.order_management.application.gateway.customer.CustomerEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@Configuration
public class AddressGatewayConfiguration {

    @Bean
    public MessageChannel addressChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setFailover(false);
        return directChannel;
    }

    @Bean
    public IntegrationFlow addressFlow() {
        return IntegrationFlow.from("addressChannel")
                .handle(Http.outboundGateway("http://localhost:8081/api/customers/{customerId}/addresses/{addressId}")
                        .uriVariable("customerId", "payload[0]")
                        .uriVariable("addressId", "payload[1]")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(AddressEntity.class)
                )
                .log()
                .bridge().get();
    }

}
