package br.com.fiap.product_import.application.service;

import br.com.fiap.product_import.domain.model.ImportFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProductImportServiceIT {

    @Autowired
    private ProductImportService productImportService;

    @Test
    public void shouldExecuteImportFileSuccessfully() {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory("./files/")
                .bytes(new byte[]{1, 2, 3})
                .build();

        // Act
        productImportService.execute(importFile);
    }

}
