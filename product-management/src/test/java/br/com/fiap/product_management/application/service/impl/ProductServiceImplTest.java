package br.com.fiap.product_management.application.service.impl;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import br.com.fiap.product_management.infra.exception.ProductException;
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

public class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        productService = new ProductServiceImpl(productRepository);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldCreateProduct() {
        // Arrange
        Product product = ProductHelper.createProduct();

        CreateProductInput productInput = ProductHelper.createProductInput();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product savedProduct = productService.create(productInput);

        // Assert
        assertThat(savedProduct).isInstanceOf(Product.class).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getCode()).isEqualTo(product.getCode());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getCategory()).isEqualTo(product.getCategory());
        assertThat(savedProduct.isEnable()).isEqualTo(product.isEnable());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
        assertThat(savedProduct.getWeight()).isEqualTo(product.getWeight());
        assertThat(savedProduct.getStockQuantity()).isEqualTo(product.getStockQuantity());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void shouldUpdateProduct() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        UpdateProductInput updateProductInput = UpdateProductInput.builder()
                .code("IP16PM")
                .name("IPhone 16 Pro Max")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .price(8000.0)
                .weight(1.0)
                .build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

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

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldEnableProduct() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        Product enabledProduct = productService.enable(productId);

        // Assert
        assertThat(enabledProduct).isNotNull().isInstanceOf(Product.class);
        assertThat(enabledProduct.isEnable()).isTrue();

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldDisableProduct() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Act
        Product disabledProduct = productService.disable(productId);

        // Assert
        assertThat(disabledProduct).isNotNull().isInstanceOf(Product.class);
        assertThat(disabledProduct.isEnable()).isFalse();

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldFindProductById() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

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

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    public void shouldFindAllProductsById() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        long productId2 = 1L;
        Product product2 = ProductHelper.createProduct();
        product2.setId(productId2);
        var products = List.of(product, product2);

        when(productRepository.findAllById(anyList())).thenReturn(products);

        // Act
        var foundProducts = productService.findAllById(Arrays.asList(productId, productId));

        // Assert
        verify(productRepository, times(1)).findAllById(anyList());
        assertThat(foundProducts).hasSize(2).contains(product, product2);
    }

    @Test
    public void shouldFindAllProductsByName() {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        long productId2 = 1L;
        Product product2 = ProductHelper.createProduct();
        product2.setId(productId2);
        var products = List.of(product, product2);

        when(productRepository.findAllByNameLike(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(products));

        // Act
        var foundProducts = productService.findAllByName(product.getName(), PageRequest.of(0, 10));

        // Assert
        verify(productRepository, times(1)).findAllByNameLike(anyString(), any(Pageable.class));
        assertThat(foundProducts).hasSize(2).contains(product, product2);
    }


}