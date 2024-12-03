package br.com.fiap.product_management.application.service.impl;

import br.com.fiap.product_management.application.input.UpdateStockInput;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import br.com.fiap.product_management.infra.exception.ProductException;
import br.com.fiap.product_management.utils.ProductHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Transactional
public class ProductStockServiceImplIT {

    @Autowired
    private ProductStockServiceImpl productStockService;
    @Autowired
    private ProductRepository productRepository;

    @Test
    public void shouldIncreaseProductStock() {
        // Arrange
        Product product = ProductHelper.saveProduct(productRepository);
        long productId = product.getId();

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        // Act
        productStockService.increase(updateStockInput);
        var foundProductOptional = productRepository.findById(productId);

        // Assert
        assertThat(foundProductOptional).isPresent();
        foundProductOptional.ifPresent(foundProduct -> {
            assertThat(foundProduct.getId()).isEqualTo(product.getId());
            assertThat(foundProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());
        });
    }

    @Test
    public void shouldDecreaseProductStock() {
        // Arrange
        Product product = ProductHelper.saveProduct(productRepository);
        long productId = product.getId();

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        // Act
        productStockService.decrease(updateStockInput);
        var foundProductOptional = productRepository.findById(productId);

        // Assert
        assertThat(foundProductOptional).isPresent();
        foundProductOptional.ifPresent(foundProduct -> {
            assertThat(foundProduct.getId()).isEqualTo(product.getId());
            assertThat(foundProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());
        });
    }

    @Test
    public void shouldThrowException_WhenDecreaseProductStock_WithInsufficientStockQuantity() {
        // Arrange
        Product product = ProductHelper.saveProduct(productRepository);
        long productId = product.getId();

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 101);

        // Act
        // Assert
        assertThatThrownBy(() -> productStockService.decrease(updateStockInput))
                .isInstanceOf(ProductException.class)
                .hasMessage("Insufficient product stock");
    }

}