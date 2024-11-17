package br.com.fiap.product_management.application.service;

import br.com.fiap.product_management.application.input.UpdateStockInput;

public interface ProductStockService {

    void increase(UpdateStockInput input);

    void decrease(UpdateStockInput input);

}
