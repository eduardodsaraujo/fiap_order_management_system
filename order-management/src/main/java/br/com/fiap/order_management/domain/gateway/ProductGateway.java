package br.com.fiap.order_management.domain.gateway;

import br.com.fiap.order_management.domain.model.Product;

import java.util.List;

public interface ProductGateway {

    void increaseStock(long productId, double quantity);

    void decreaseStock(long productId, double quantity);

    List<Product> findAllByIds(List<Long> productsIds);

}
