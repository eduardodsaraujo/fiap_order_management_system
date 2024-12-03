package br.com.fiap.product_import.application.service;

import br.com.fiap.product_import.domain.model.ImportFile;
import br.com.fiap.product_import.gateway.FileGateway;
import br.com.fiap.product_import.gateway.JobGateway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ProductImportServiceTest {

    private ProductImportService productImportService;

    @Mock
    private FileGateway fileGateway;
    @Mock
    private JobGateway jobGateway;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        productImportService = new ProductImportService(fileGateway, jobGateway);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldImportFile() {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory("./files/")
                .bytes(new byte[]{1, 2, 3})
                .build();

        doNothing().when(fileGateway).save(any(ImportFile.class));
        doNothing().when(jobGateway).execute(any(ImportFile.class));

        // Act
        productImportService.execute(importFile);

        // Assert
        verify(fileGateway, times(1)).save(importFile);
        verify(jobGateway, times(1)).execute(importFile);
    }

}
