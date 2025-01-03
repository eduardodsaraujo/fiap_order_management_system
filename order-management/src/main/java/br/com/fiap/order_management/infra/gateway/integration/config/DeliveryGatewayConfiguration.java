package br.com.fiap.order_management.infra.gateway.integration.config;

import br.com.fiap.order_management.infra.gateway.integration.dto.delivery.CalculateShippingDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@Configuration
public class DeliveryGatewayConfiguration {

    @Bean
    public MessageChannel calculateShippingChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setFailover(false);
        return directChannel;
    }

    @Bean
    public IntegrationFlow calculateShippingFlow() {
        return IntegrationFlow.from("calculateShippingChannel")
                .handle(Http.outboundGateway("http://localhost:8080/delivery-logistics/api/delivery/calculate-shipping")
                        .httpMethod(HttpMethod.POST)
                        .expectedResponseType(CalculateShippingDto.class)
                )
                .bridge().get();
    }

    @Bean
    public MessageChannel createDeliveryChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setFailover(false);
        return directChannel;
    }

    @Bean
    public IntegrationFlow createDeliveryFlow() {
        return IntegrationFlow.from("createDeliveryChannel")
                .handle(Http.outboundGateway("http://localhost:8080/delivery-logistics/api/delivery")
                        .httpMethod(HttpMethod.POST)
                        .expectedResponseType(CalculateShippingDto.class)
                )
                .bridge().get();
    }

}
