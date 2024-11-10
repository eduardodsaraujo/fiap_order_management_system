package br.com.fiap.order_management.infra.config;

import br.com.fiap.order_management.application.gateway.customer.CustomerEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@Configuration
public class CustomerGatewayConfiguration {

    @Bean
    public MessageChannel customerChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setFailover(false);
        return directChannel;
    }

    @Bean
    public IntegrationFlow customerFlow() {
        return IntegrationFlow.from("customerChannel")
                .handle(Http.outboundGateway("http://localhost:8081/api/customers/{customerId}")
                        .uriVariable("customerId", "payload")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(CustomerEntity.class)
                )
                .log()
                .bridge().get();
    }

}
