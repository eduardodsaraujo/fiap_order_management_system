package br.com.fiap.product_management.application.service.impl;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import br.com.fiap.product_management.utils.ProductHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class ProductServiceImplIT {

    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldCreateProduct() {
        // Arrange
        CreateProductInput productInput = ProductHelper.createProductInput();

        // Act
        Product savedProduct = productService.create(productInput);

        // Assert
        assertThat(savedProduct).isInstanceOf(Product.class).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
        assertThat(savedProduct.getCode()).isEqualTo(productInput.getCode());
        assertThat(savedProduct.getName()).isEqualTo(productInput.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(productInput.getDescription());
        assertThat(savedProduct.getCategory()).isEqualTo(productInput.getCategory());
        assertThat(savedProduct.isEnable()).isEqualTo(true);
        assertThat(savedProduct.getPrice()).isEqualTo(productInput.getPrice());
        assertThat(savedProduct.getWeight()).isEqualTo(productInput.getWeight());
        assertThat(savedProduct.getStockQuantity()).isEqualTo(productInput.getStockQuantity());
    }

    @Test
    public void shouldUpdateProduct() {
        // Arrange
        long productId = 1L;

        UpdateProductInput updateProductInput = UpdateProductInput.builder()
                .code("IP16PM")
                .name("IPhone 16 Pro Max")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .price(8000.0)
                .weight(1.0)
                .build();

        // Act
        Product updatedProduct = productService.update(productId, updateProductInput);

        // Assert
        assertThat(updatedProduct).isNotNull().isInstanceOf(Product.class);
        assertThat(updatedProduct.getId()).isEqualTo(productId);
        assertThat(updatedProduct.getCode()).isEqualTo("IP16PM");
        assertThat(updatedProduct.getName()).isEqualTo("IPhone 16 Pro Max");
        assertThat(updatedProduct.getDescription()).isEqualTo("IPhone 16 Pro 128GB 6 Polegadas");
        assertThat(updatedProduct.getCategory()).isEqualTo("Smartphone");
        assertThat(updatedProduct.getManufacturer()).isEqualTo("manufacturer");
        assertThat(updatedProduct.getPrice()).isEqualTo(8000.0);
        assertThat(updatedProduct.getWeight()).isEqualTo(1.0);
    }

    @Test
    public void shouldEnableProduct() {
        // Arrange
        long productId = 1L;

        // Act
        Product enabledProduct = productService.enable(productId);

        // Assert
        assertThat(enabledProduct).isNotNull().isInstanceOf(Product.class);
        assertThat(enabledProduct.isEnable()).isTrue();
    }

    @Test
    public void shouldDisableProduct() {
        // Arrange
        long productId = 1L;

        // Act
        Product disabledProduct = productService.disable(productId);

        // Assert
        assertThat(disabledProduct).isNotNull().isInstanceOf(Product.class);
        assertThat(disabledProduct.isEnable()).isFalse();
    }

    @Test
    public void shouldFindProductById() {
        // Arrange
        Product product = ProductHelper.saveProduct(productRepository);
        long productId = product.getId();

        // Act
        Product foundProduct = productService.findById(productId);

        // Assert
        assertThat(foundProduct).isNotNull().isInstanceOf(Product.class);
        assertThat(foundProduct.getId()).isEqualTo(product.getId());
        assertThat(foundProduct.getCode()).isEqualTo(product.getCode());
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
        assertThat(foundProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(foundProduct.getCategory()).isEqualTo(product.getCategory());
        assertThat(foundProduct.isEnable()).isEqualTo(product.isEnable());
        assertThat(foundProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(foundProduct.getWeight()).isEqualTo(product.getWeight());
        assertThat(foundProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());
    }

    @Test
    public void shouldFindAllProductsById() {
        // Arrange
        long productId = 1L;
        long productId2 = 2L;

        // Act
        var foundProducts = productService.findAllById(Arrays.asList(productId, productId2));

        // Assert
        assertThat(foundProducts).hasSize(2);
    }

    @Test
    public void shouldFindAllProductsByName() {
        // Arrange
        // Act
        var foundProducts = productService.findAllByName("IP%", PageRequest.of(0, 10));

        // Assert
        assertThat(foundProducts).hasSize(2);
    }


}