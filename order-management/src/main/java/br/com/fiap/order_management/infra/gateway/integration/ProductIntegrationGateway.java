package br.com.fiap.order_management.infra.gateway.integration;

import br.com.fiap.order_management.domain.gateway.ProductGateway;
import br.com.fiap.order_management.domain.model.Product;
import br.com.fiap.order_management.infra.gateway.integration.mapper.ProductDtoMapper;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductDto;
import br.com.fiap.order_management.infra.gateway.integration.dto.product.ProductStockRequestDto;
import br.com.fiap.order_management.infra.gateway.integration.gateway.ProductWsGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductIntegrationGateway implements ProductGateway {

    private final ProductWsGateway productWsGateway;

    @Override
    public List<Product> findAllByIds(List<Long> productsIds) {
        List<ProductDto> productsDto = productWsGateway.findAllByIds(productsIds);
        return productsDto.stream().map(ProductDtoMapper::toDomain).toList();
    }

}
