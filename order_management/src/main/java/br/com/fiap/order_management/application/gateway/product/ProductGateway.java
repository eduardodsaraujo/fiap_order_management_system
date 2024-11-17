package br.com.fiap.order_management.application.gateway.product;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHeaders;

import java.util.List;
import java.util.UUID;

@MessagingGateway
public interface ProductGateway {

    @Gateway(requestChannel = "productChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    List<ProductEntity> findAllByIds(List<Long> productsIds);

    @Gateway(requestChannel = "decreaseProductStockChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    void increaseStock(ProductStockInput message);

    @Gateway(requestChannel = "increaseProductStockChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    void decreaseStock(ProductStockInput message);

}
