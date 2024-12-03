package br.com.fiap.product_management.api.controller;

import br.com.fiap.product_management.application.input.UpdateStockInput;
import br.com.fiap.product_management.application.service.ProductStockService;
import br.com.fiap.product_management.domain.model.Product;
import br.com.fiap.product_management.infra.exception.GlobalExceptionHandler;
import br.com.fiap.product_management.infra.exception.ProductException;
import br.com.fiap.product_management.utils.ProductHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static br.com.fiap.product_management.utils.JsonHelper.asJsonString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductStockControllerIT {

    private MockMvc mockMvc;

    @Mock
    private ProductStockService productStockService;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        ProductStockController productStockController = new ProductStockController(productStockService);

        mockMvc = MockMvcBuilders.standaloneSetup(productStockController)
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
    public void shouldIncreaseProductStock() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        doNothing().when(productStockService).increase(any(UpdateStockInput.class));

        // Act
        mockMvc.perform(put("/api/products/stock/increase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateStockInput)))
                .andExpect(status().isOk());

        // Assert
        verify(productStockService, times(1)).increase(any(UpdateStockInput.class));
    }

    @Test
    public void shouldDecreaseProductStock() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 10);

        doNothing().when(productStockService).decrease(any(UpdateStockInput.class));

        // Act
        mockMvc.perform(put("/api/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateStockInput)))
                .andExpect(status().isOk());

        // Assert
        verify(productStockService, times(1)).decrease(any(UpdateStockInput.class));
    }

    @Test
    public void shouldThrowException_WhenDecreaseProductStock_WithInsufficientStockQuantity() throws Exception {
        // Arrange
        long productId = 1L;
        Product product = ProductHelper.createProduct();
        product.setId(productId);

        UpdateStockInput updateStockInput = new UpdateStockInput(productId, 101);

        doThrow(new ProductException("Insufficient product stock"))
                .when(productStockService).decrease(any(UpdateStockInput.class));

        // Act
        mockMvc.perform(put("/api/products/stock/decrease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(updateStockInput)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Insufficient product stock"));

        // Assert
        verify(productStockService, times(1)).decrease(any(UpdateStockInput.class));
    }
}