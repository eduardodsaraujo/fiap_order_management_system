package br.com.fiap.order_management.domain.gateway;

import br.com.fiap.order_management.domain.model.Product;

import java.util.List;

public interface ProductGateway {

    List<Product> findAllByIds(List<Long> productsIds);

}
