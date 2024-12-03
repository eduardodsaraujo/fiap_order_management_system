package br.com.fiap.product_management.domain.repository;

import br.com.fiap.product_management.domain.model.Product;
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
public class ProductRepositoryIT {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldSaveProduct() {
        // Arrange
        Product product = ProductHelper.createProduct();

        // Act
        Product savedProduct = productRepository.save(product);

        // Assert
        assertThat(savedProduct).isNotNull().isEqualTo(product);
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getCode()).isEqualTo(product.getCode());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getCategory()).isEqualTo(product.getCategory());
        assertThat(savedProduct.isEnable()).isEqualTo(product.isEnable());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getWeight()).isEqualTo(product.getWeight());
        assertThat(savedProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());
    }

    @Test
    public void shouldFindProductById() {
        // Arrange
        Product product = ProductHelper.saveProduct(productRepository);
        long productId = product.getId();

        // Act
        var foundProductOptional = productRepository.findById(productId);

        // Assert
        assertThat(foundProductOptional).isPresent().containsSame(product);
        foundProductOptional.ifPresent(foundProduct -> {
            assertThat(foundProduct.getId()).isEqualTo(product.getId());
            assertThat(foundProduct.getCode()).isEqualTo(product.getCode());
            assertThat(foundProduct.getName()).isEqualTo(product.getName());
            assertThat(foundProduct.getDescription()).isEqualTo(product.getDescription());
            assertThat(foundProduct.getCategory()).isEqualTo(product.getCategory());
            assertThat(foundProduct.isEnable()).isEqualTo(product.isEnable());
            assertThat(foundProduct.getPrice()).isEqualTo(product.getPrice());
            assertThat(foundProduct.getWeight()).isEqualTo(product.getWeight());
            assertThat(foundProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());
        });
    }

    @Test
    public void shouldDeleteProductById() {
        // Arrange
        Product product = ProductHelper.saveProduct(productRepository);
        long productId = product.getId();

        // Act
        productRepository.deleteById(productId);
        var foundProductOptional = productRepository.findById(productId);

        // Assert
        assertThat(foundProductOptional).isEmpty();
    }

    @Test
    public void shouldFindAllProductsById() {
        // Arrange
        long prosuctId1 = 1L;
        long prosuctId2 = 2L;

        // Act
        var foundProducts = productRepository.findAllById(Arrays.asList(prosuctId1, prosuctId2));

        // Assert
        assertThat(foundProducts).hasSize(2);
    }

    @Test
    public void shouldFindAllProducts() {
        // Arrange
        // Act
        var foundProducts = productRepository.findAll();

        // Assert
        assertThat(foundProducts).hasSize(2);
    }

    @Test
    public void shouldFindAllProductsByNameLike() {
        // Arrange
        // Act
        var foundProducts = productRepository.findAllByNameLike("IP%", PageRequest.of(0, 10));

        // Assert
        assertThat(foundProducts).hasSize(2);
    }

}