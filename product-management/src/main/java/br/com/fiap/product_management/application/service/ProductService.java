package br.com.fiap.product_management.application.service;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {

    Product create(CreateProductInput input);

    Product update(long productId, UpdateProductInput input);

    Product enable(long productId);

    Product disable(long productId);

    Product findById(long productId);

    List<Product> findAllById(List<Long> productsIds);

    Page<Product> findAllByName(String name, Pageable pageable);

}
