package br.com.fiap.order_management.infra.gateway.integration.gateway;

import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductStockRequestDto;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.GatewayHeader;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.MessageHeaders;

import java.util.List;

@MessagingGateway
public interface ProductWsGateway {

    @Gateway(requestChannel = "productChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    List<ProductDto> findAllByIds(List<Long> productsIds);

    @Gateway(requestChannel = "increaseProductStockChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    void increaseStock(ProductStockRequestDto message);

    @Gateway(requestChannel = "decreaseProductStockChannel", requestTimeout = 5000,
            headers = @GatewayHeader(name = MessageHeaders.REPLY_CHANNEL, expression = "@nullChannel"))
    void decreaseStock(ProductStockRequestDto message);

}
