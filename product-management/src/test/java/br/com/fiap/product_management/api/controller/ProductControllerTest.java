package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.application.input.CreateProductInput;
import br.com.fiap.product_management.application.input.UpdateProductInput;
import br.com.fiap.product_management.application.service.ProductService;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.infra.exception.GlobalExceptionHandler;
import br.com.fiap.product_management.utils.ProductHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static br.com.fiap.product_management.utils.JsonHelper.asJsonString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        ProductController productController = new ProductController(productService);

        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();

    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldCreateProduct() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        CreateProductInput productInput = ProductHelper.createProductInput();

        when(productService.create(any(CreateProductInput.class))).thenReturn(product);

        // Act
        mockMvc.perform(post("/api/products", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(productInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.category").value(product.getCategory()))
                .andExpect(jsonPath("$.enable").value(product.isEnable()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.weight").value(product.getWeight()));

        // Assert
        verify(productService, times(1)).create(any(CreateProductInput.class));
    }

    @Test
    public void shouldUpdateProduct() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .code("IP16PM")
                .name("IPhone 16 Pro Max")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .enable(true)
                .price(8000.0)
                .weight(1.0)
                .build();

        UpdateProductInput updateProductInput = UpdateProductInput.builder()
                .code("IP16PM")
                .name("IPhone 16 Pro Max")
                .description("IPhone 16 Pro 128GB 6 Polegadas")
                .category("Smartphone")
                .manufacturer("manufacturer")
                .price(8000.0)
                .weight(1.0)
                .build();

        when(productService.update(anyLong(), any(UpdateProductInput.class))).thenReturn(product);

        // Act
        mockMvc.perform(put("/api/products/{productId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateProductInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.category").value(product.getCategory()))
                .andExpect(jsonPath("$.enable").value(true))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.weight").value(product.getWeight()));

        // Assert
        verify(productService, times(1)).update(anyLong(), any(UpdateProductInput.class));
    }

    @Test
    public void shouldEnableProduct() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        when(productService.enable(anyLong())).thenReturn(product);

        // Act
        mockMvc.perform(put("/api/products/{productId}/enable", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.enable").value(true));

        // Assert
        verify(productService, times(1)).enable(anyLong());
    }

    @Test
    public void shouldDisableProduct() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);
        product.setEnable(false);

        when(productService.disable(anyLong())).thenReturn(product);

        // Act
        mockMvc.perform(put("/api/products/{productId}/disable", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId))
                .andExpect(jsonPath("$.enable").value(false));

        // Assert
        verify(productService, times(1)).disable(anyLong());
    }

    @Test
    public void shouldFindProductById() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);
        product.setEnable(false);

        when(productService.findById(anyLong())).thenReturn(product);

        // Act
        mockMvc.perform(get("/api/products/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.code").value(product.getCode()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.category").value(product.getCategory()))
                .andExpect(jsonPath("$.enable").value(product.isEnable()))
                .andExpect(jsonPath("$.price").value(product.getPrice()))
                .andExpect(jsonPath("$.weight").value(product.getWeight()));

        // Assert
        verify(productService, times(1)).findById(anyLong());
    }

    @Test
    public void shouldFindAllProductsById() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        long productId2 = 1L;
        Product product2 = ProductHelper.createProduct();
        product2.setId(productId2);
        var products = List.of(product, product2);

        when(productService.findAllById(anyList())).thenReturn(products);

        // Act
        mockMvc.perform(get("/api/products/all/{productsId}", Arrays.asList(productId, productId2))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(productId))
                .andExpect(jsonPath("$[0].code").value(product.getCode()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].description").value(product.getDescription()))
                .andExpect(jsonPath("$[0].category").value(product.getCategory()))
                .andExpect(jsonPath("$[0].enable").value(product.isEnable()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].weight").value(product.getWeight()));

        // Assert
        verify(productService, times(1)).findAllById(anyList());
    }

    @Test
    public void shouldFindAllProductsByName() throws Exception {
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        long productId2 = 1L;
        Product product2 = ProductHelper.createProduct();
        product2.setId(productId2);
        var products = List.of(product, product2);

        when(productService.findAllByName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<>(products));

        // Act
        mockMvc.perform(get("/api/products", Arrays.asList(productId, productId2))
                        .queryParam("name", product.getName())
                        .queryParam("page", "0")
                        .queryParam("size", "10")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(productId))
                .andExpect(jsonPath("$[0].code").value(product.getCode()))
                .andExpect(jsonPath("$[0].name").value(product.getName()))
                .andExpect(jsonPath("$[0].description").value(product.getDescription()))
                .andExpect(jsonPath("$[0].category").value(product.getCategory()))
                .andExpect(jsonPath("$[0].enable").value(product.isEnable()))
                .andExpect(jsonPath("$[0].price").value(product.getPrice()))
                .andExpect(jsonPath("$[0].weight").value(product.getWeight()));

        // Assert
        verify(productService, times(1)).findAllByName(anyString(), any(Pageable.class));
    }

}