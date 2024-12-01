package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.gateway.StockProductGateway;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductStockRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.ProductWsGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockProductIntegrationGateway implements StockProductGateway {

    private final ProductWsGateway productWsGateway;

    @Override
    public void increaseStock(long productId, double quantity) {
        ProductStockRequestDto requestDto = new ProductStockRequestDto(productId, quantity);
        productWsGateway.increaseStock(requestDto);
    }

    @Override
    public void decreaseStock(long productId, double quantity) {
        ProductStockRequestDto requestDto = new ProductStockRequestDto(productId, quantity);
        productWsGateway.decreaseStock(requestDto);
    }

}
