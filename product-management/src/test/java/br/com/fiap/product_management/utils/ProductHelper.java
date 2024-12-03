package br.com.fiap.product_management.utils;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;

public class ProductHelper {

    public static Product createProduct() {
        return Product.builder()
                .code("IP16P")
                .name("IPhone 16 Pro")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .enable(true)
                .price(8000.0)
                .weight(1.0)
                .stockQuantity(100.0)
                .build();
    }

    public static CreateProductInput createProductInput() {
        return CreateProductInput.builder()
                .code("IP16P")
                .name("IPhone 16 Pro")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .price(8000.0)
                .weight(1.0)
                .stockQuantity(100.0)
                .build();
    }

    public static Product saveProduct(ProductRepository productRepository) {
        Product product = createProduct();
        return productRepository.save(product);
    }

}
