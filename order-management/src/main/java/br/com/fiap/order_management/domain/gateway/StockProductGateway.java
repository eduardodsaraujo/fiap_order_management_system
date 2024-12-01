package br.com.fiap.order_management.domain.gateway;

public interface StockProductGateway {

    void increaseStock(long productId, double quantity);

    void decreaseStock(long productId, double quantity);

}
