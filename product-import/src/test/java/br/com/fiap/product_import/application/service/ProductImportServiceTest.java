package br.com.fiap.product_import.application.service;

import br.com.fiap.product_import.domain.model.ImportFile;
import br.com.fiap.product_import.gateway.FileGateway;
import br.com.fiap.product_import.gateway.JobGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ProductImportServiceTest {

    @InjectMocks
    private ProductImportService productImportService;

    @Mock
    private FileGateway fileGateway;

    @Mock
    private JobGateway jobGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldExecuteImportFileSuccessfully() {
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory("/files/")
                .bytes(new byte[]{1, 2, 3})
                .build();

        productImportService.execute(importFile);

        verify(fileGateway, times(1)).save(importFile);
        verify(jobGateway, times(1)).execute(importFile);
    }
}
