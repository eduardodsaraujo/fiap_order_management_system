package br.com.fiap.product_management.application.service.impl;

import br.com.fiap.product_management.application.input.UpdateStockInput;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import br.com.fiap.product_management.infra.exception.ProductException;
import br.com.fiap.product_management.utils.ProductHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductStockServiceImplTest {

    private ProductStockServiceImpl productStockService;

    @Mock
    private ProductRepository productRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        productStockService = new ProductStockServiceImpl(productRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldIncreaseProductStock() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        productStockService.increase(updateStockInput);

        // Assert
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldDecreaseProductStock() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        productStockService.decrease(updateStockInput);

        // Assert
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldThrowException_WhenDecreaseProductStock_WithInsufficientStockQuantity() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 101);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        // Assert
        assertThatThrownBy(() -> productStockService.decrease(updateStockInput))
                .isInstanceOf(ProductException.class)
                .hasMessage("Insufficient product stock");
    }

}