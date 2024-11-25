package br.com.fiap.product_import.controller;

import br.com.fiap.product_import.api.controller.ProductImportController;
import br.com.fiap.product_import.application.service.ProductImportService;
import br.com.fiap.product_import.domain.model.ImportFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ProductImportControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductImportService productImportService;

    @InjectMocks
    private ProductImportController productImportController;

    @Value("${import-file.input-path}")
    private String directory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productImportController).build();
    }

    @Test
    void handleFileUpload_ShouldReturnOk() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "data.csv", MediaType.MULTIPART_FORM_DATA_VALUE, "content".getBytes());

        doNothing().when(productImportService).execute(any(ImportFile.class));

        mockMvc.perform(multipart("/api/product-import")
                        .file(file)
                        .param("file", "data.csv"))
                .andExpect(status().isOk());
    }

}
