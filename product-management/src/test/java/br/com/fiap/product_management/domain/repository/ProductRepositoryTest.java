package br.com.fiap.product_management.domain.repository;

import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.utils.ProductHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductRepositoryTest {

    @Mock
    private ProductRepository productRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldSaveProduct() {
        // Arrange
        Product product = ProductHelper.createProduct();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product savedProduct = productRepository.save(product);

        // Assert
        assertThat(savedProduct).isNotNull().isEqualTo(product);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void shouldFindProductById() {
        // Arrange
        long prosuctId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(prosuctId);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        var foundProductOptional = productRepository.findById(prosuctId);

        // Assert
        verify(productRepository, times(1)).findById(anyLong());
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
        long prosuctId = 1L;

        doNothing().when(productRepository).deleteById(anyLong());

        // Act
        productRepository.deleteById(prosuctId);

        // Assert
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void shouldFindAllProductsById() {
        // Arrange
        long prosuctId1 = 1L;
        Product product1 = ProductHelper.createProduct();
        product1.setId(prosuctId1);

        long prosuctId2 = 2L;
        Product product2 = ProductHelper.createProduct();
        product2.setId(prosuctId2);
        var products = List.of(product1, product2);

        when(productRepository.findAllById(anyList())).thenReturn(products);

        // Act
        var foundProducts = productRepository.findAllById(Arrays.asList(prosuctId1, prosuctId2));

        // Assert
        verify(productRepository, times(1)).findAllById(anyList());
        assertThat(foundProducts).hasSize(2);
    }

    @Test
    public void shouldFindAllProducts() {
        // Arrange
        Product product1 = ProductHelper.createProduct();
        Product product2 = ProductHelper.createProduct();
        var products = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(products);

        // Act
        var foundProducts = productRepository.findAll();

        // Assert
        verify(productRepository, times(1)).findAll();
        assertThat(foundProducts).hasSize(2);
    }

    @Test
    public void shouldFindAllProductsByNameLike() {
        // Arrange
        Product product1 = ProductHelper.createProduct();
        Product product2 = ProductHelper.createProduct();
        var products = List.of(product1, product2);

        when(productRepository.findAllByNameLike(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(products));

        // Act
        var foundProducts = productRepository.findAllByNameLike(product1.getName(), PageRequest.of(0, 10));

        // Assert
        verify(productRepository, times(1)).findAllByNameLike(anyString(), any(Pageable.class));
        assertThat(foundProducts).hasSize(2);
    }

}