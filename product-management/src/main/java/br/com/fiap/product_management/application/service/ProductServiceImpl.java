package br.com.fiap.product_management.application.service;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import infra.exception.ProductException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Product create(CreateProductInput input) {
        Product product = Product.builder()
                .code(input.getCode())
                .name(input.getName())
                .description(input.getDescription())
                .category(input.getCategory())
                .manufacturer(input.getManufacturer())
                .price(input.getPrice())
                .stockQuantity(input.getStockQuantity())
                .build();
        product.enable();

        productRepository.save(product);

        return product;
    }

    @Override
    public Product update(long productId, UpdateProductInput input) {
        Product product = findById(productId);
        product.setCode(input.getCode());
        product.setName(input.getName());
        product.setDescription(input.getDescription());
        product.setCategory(input.getCategory());
        product.setManufacturer(input.getManufacturer());
        product.setPrice(input.getPrice());

        return product;
    }

    @Override
    public Product enable(long productId) {
        Product product = findById(productId);
        product.enable();

        return product;
    }

    @Override
    public Product disable(long productId) {
        Product product = findById(productId);
        product.disable();

        return product;
    }

    @Override
    public Product findById(long productId) {
        return productRepository.findById(productId).orElseThrow(() -> new ProductException("Product not found"));
    }

    @Override
    public List<Product> findAllById(List<Long> productsIds) {
        return productRepository.findAllById(productsIds);
    }

    @Override
    public Page<Product> findAllByName(String name, Pageable pageable) {
        return productRepository.findAllByNameLike(name, pageable);
    }

}
