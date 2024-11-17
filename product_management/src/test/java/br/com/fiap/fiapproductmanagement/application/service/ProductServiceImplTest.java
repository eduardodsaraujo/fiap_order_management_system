package br.com.fiap.fiapproductmanagement.application.service;


import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.application.service.ProductServiceImpl;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.domain.repository.ProductRepository;
import infra.exception.ProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
    }

    @Test
    void create_shouldSaveNewProduct() {
        CreateProductInput input = CreateProductInput.builder()
                .code("123")
                .name("Product Name")
                .description("Description")
                .category("Category")
                .manufacturer("Manufacturer")
                .price(100.0)
                .stockQuantity(50)
                .build();

        Product expectedProduct = Product.builder()
                .id(1L)
                .code("123")
                .name("Product Name")
                .description("Description")
                .category("Category")
                .manufacturer("Manufacturer")
                .price(100.0)
                .stockQuantity(50)
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);
        Product actualProduct = productService.create(input);
        assertEquals(expectedProduct.getCode(), actualProduct.getCode());
        assertEquals(expectedProduct.getName(), actualProduct.getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void update_shouldUpdateProduct() {
        long productId = 1L;
        UpdateProductInput input = UpdateProductInput.builder()
                .code("456")
                .name("Updated Product Name")
                .description("Updated Description")
                .category("Updated Category")
                .manufacturer("Updated Manufacturer")
                .price(150.0)
                .build();

        Product existingProduct = Product.builder()
                .id(productId)
                .code("123")
                .name("Product Name")
                .description("Description")
                .category("Category")
                .manufacturer("Manufacturer")
                .price(100.0)
                .stockQuantity(50)
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        Product updatedProduct = productService.update(productId, input);
        assertEquals(input.getCode(), updatedProduct.getCode());
        assertEquals(input.getName(), updatedProduct.getName());
        verify(productRepository).findById(productId);
    }

    @Test
    void findById_shouldThrowProductExceptionWhenProductNotFound() {
        long productId = 1L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());
        ProductException exception = assertThrows(ProductException.class, () -> productService.findById(productId));
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void enable_shouldEnableProduct() {
        long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .enable(false)
                .build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Product enabledProduct = productService.enable(productId);
        assertTrue(enabledProduct.isEnable());
        verify(productRepository).findById(productId);
    }

    @Test
    void disable_shouldDisableProduct() {
        long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .enable(true)
                .build();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Product disabledProduct = productService.disable(productId);
        assertFalse(disabledProduct.isEnable());
        verify(productRepository).findById(productId);
    }

    @Test
    void findAllByName_shouldReturnProducts() {
        String name = "Product";
        Pageable pageable = mock(Pageable.class);
        Page<Product> productPage = mock(Page.class);
        when(productRepository.findAllByNameLike(eq(name), eq(pageable))).thenReturn(productPage);
        Page<Product> result = productService.findAllByName(name, pageable);
        assertEquals(productPage, result);
        verify(productRepository).findAllByNameLike(eq(name), eq(pageable));
    }
}
