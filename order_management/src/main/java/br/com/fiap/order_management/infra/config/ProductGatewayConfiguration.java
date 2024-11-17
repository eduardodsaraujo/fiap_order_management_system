package br.com.fiap.order_management.infra.config;

import br.com.fiap.order_management.application.gateway.product.ProductEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.http.dsl.Http;
import org.springframework.messaging.MessageChannel;

@Configuration
public class ProductGatewayConfiguration {

    @Bean
    public MessageChannel productChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setFailover(false);
        return directChannel;
    }

    @Bean
    public IntegrationFlow productFlow() {
        return IntegrationFlow.from("productChannel")
                .handle(Http.outboundGateway("http://localhost:8080/product-management/api/products/all/{productsIds}")
                        .uriVariable("productsIds", "payload")
                        .httpMethod(HttpMethod.GET)
                        .expectedResponseType(ProductEntity[].class)
                )
                .log()
                .bridge().get();
    }

    @Bean
    public MessageChannel increaseProductStockChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setFailover(false);
        return directChannel;
    }

    @Bean
    public IntegrationFlow increaseProductStockFlow() {
        return IntegrationFlow.from("increaseProductStockChannel")
                .handle(Http.outboundGateway("http://localhost:8080/product-management/api/products/stock/increase")
                        .httpMethod(HttpMethod.PUT)
                )
                .log()
                .bridge().get();
    }

    @Bean
    public MessageChannel decreaseProductStockChannel() {
        DirectChannel directChannel = new DirectChannel();
        directChannel.setFailover(false);
        return directChannel;
    }

    @Bean
    public IntegrationFlow decreaseProductStockFlow() {
        return IntegrationFlow.from("decreaseProductStockChannel")
                .handle(Http.outboundGateway("http://localhost:8080/product-management/api/products/stock/decrease")
                        .httpMethod(HttpMethod.PUT)
                )
                .log()
                .bridge().get();
    }


}
