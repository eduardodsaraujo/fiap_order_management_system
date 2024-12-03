package br.com.fiap.product_import.api.controller;

import br.com.fiap.product_import.application.service.ProductImportService;
import br.com.fiap.product_import.domain.model.ImportFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductImportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductImportService productImportService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ProductImportController productImportController = new ProductImportController(productImportService);
        mockMvc = MockMvcBuilders.standaloneSetup(productImportController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @Test
    public void shouldImportProducts() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "product.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "IP1P;iphone 1 pro;iphone 16 pro 256;Smartphone;Apple;true;9000;2;20".getBytes()
        );

        doNothing().when(productImportService).execute(any(ImportFile.class));

        // Act
        mockMvc.perform(multipart("/api/product-import")
                        .file(file))
                .andExpect(status().isOk());

        // Assert
        verify(productImportService, times(1)).execute(any(ImportFile.class));
    }


    @Test
    public void shouldThrowException_WhenSaveFile_WithIncorrectFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "product.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "IP1P;iphone 1 pro;iphone 16 pro 256;Smartphone;Apple;true;9000;2;20".getBytes()
        );

        doThrow(NullPointerException.class).when(productImportService).execute(any(ImportFile.class));


        // Act
        // Assert
        mockMvc.perform(multipart("/api/product-import")
                        .file(file))
                .andExpect(status().isInternalServerError());
    }

}