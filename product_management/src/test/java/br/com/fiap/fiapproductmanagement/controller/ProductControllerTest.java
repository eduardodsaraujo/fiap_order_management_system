package br.com.fiap.fiapproductmanagement.controller;

import br.com.fiap.product_management.api.controller.ProductController;
import br.com.fiap.product_management.api.controller.dto.ProductDto;
import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.application.service.ProductService;
import br.com.fiap.product_management.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        CreateProductInput input = CreateProductInput.builder()
                .code("P123")
                .name("Product Name")
                .description("Product Description")
                .category("Category")
                .manufacturer("Manufacturer")
                .price(100.0)
                .stockQuantity(10.0)
                .build();

        Product product = new Product(1L, "P123", "Product Name", "Product Description", "Category", "Manufacturer", true, 100.0, 2.0, 10.0);
        when(productService.create(any(CreateProductInput.class))).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.create(input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("P123", response.getBody().getCode());
        verify(productService, times(1)).create(any(CreateProductInput.class));
    }

    @Test
    void testUpdate() {
        UpdateProductInput input = UpdateProductInput.builder()
                .code("P123")
                .name("Updated Name")
                .description("Updated Description")
                .category("Updated Category")
                .manufacturer("Updated Manufacturer")
                .price(150.0)
                .build();

        Product product = new Product(1L, "P123", "Updated Name", "Updated Description", "Updated Category", "Updated Manufacturer", true, 150.0, 10.0);
        when(productService.update(eq(1L), any(UpdateProductInput.class))).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.update(1L, input);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
        verify(productService, times(1)).update(eq(1L), any(UpdateProductInput.class));
    }

    @Test
    void testEnable() {
        Product product = new Product(1L, "P123", "Product Name", "Product Description", "Category", "Manufacturer", true, 100.0, 10.0);
        when(productService.enable(1L)).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.enable(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("P123", response.getBody().getCode());
        verify(productService, times(1)).enable(1L);
    }

    @Test
    void testDisable() {
        Product product = new Product(1L, "P123", "Product Name", "Product Description", "Category", "Manufacturer", false, 100.0, 10.0);
        when(productService.disable(1L)).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.disable(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("P123", response.getBody().getCode());
        verify(productService, times(1)).disable(1L);
    }

    @Test
    void testFindById() {
        Product product = new Product(1L, "P123", "Product Name", "Product Description", "Category", "Manufacturer", true, 100.0, 10.0);
        when(productService.findById(1L)).thenReturn(product);

        ResponseEntity<ProductDto> response = productController.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("P123", response.getBody().getCode());
        verify(productService, times(1)).findById(1L);
    }

    @Test
    void testFindAllByName() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = new Product(1L, "P123", "Product Name", "Product Description", "Category", "Manufacturer", true, 100.0, 10.0);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        when(productService.findAllByName("Product Name", pageable)).thenReturn(productPage);

        ResponseEntity<Page<ProductDto>> response = productController.findAllByName("Product Name", pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals("P123", response.getBody().getContent().get(0).getCode());
        verify(productService, times(1)).findAllByName("Product Name", pageable);
    }
}
