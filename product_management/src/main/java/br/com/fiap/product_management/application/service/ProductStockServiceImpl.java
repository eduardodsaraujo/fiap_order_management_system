package br.com.fiap.product_management.application.service;

import br.com.fiap.product_management.application.input.UpdateStockInput;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import infra.exception.ProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductStockServiceImpl implements ProductStockService{

    private final ProductRepository productRepository;

    @Override
    public void increase(UpdateStockInput input) {
        Product product = productRepository.findById(input.getProductId())
                .orElseThrow(() -> new ProductException("Product not found"));

        product.increase(input.getQuantity());
    }

    @Override
    public void decrease(UpdateStockInput input) {
        Product product = productRepository.findById(input.getProductId())
                .orElseThrow(() -> new ProductException("Product not found"));

        product.decrease(input.getQuantity());
    }

}
