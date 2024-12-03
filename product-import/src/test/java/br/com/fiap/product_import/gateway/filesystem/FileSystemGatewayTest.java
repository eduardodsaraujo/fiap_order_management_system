package br.com.fiap.product_import.gateway.filesystem;

import br.com.fiap.product_import.domain.model.ImportFile;
import br.com.fiap.product_import.infra.exception.ProductImportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileSystemGatewayTest {

    private FileSystemGateway fileSystemGateway;

    @BeforeEach
    public void setup() {
        fileSystemGateway = new FileSystemGateway();
    }

    @Test
    public void shouldSaveFile(){
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory("./files/")
                .bytes(new byte[]{1, 2, 3})
                .build();

        fileSystemGateway.save(importFile);
    }

    @Test
    public void shouldThrowException_WhenSaveFile_WithIncorrectPath() {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory("")
                .bytes(new byte[]{1, 2, 3})
                .build();

        // Act
        // Assert
        assertThatThrownBy(() -> fileSystemGateway.save(importFile))
                .isInstanceOf(ProductImportException.class)
                .hasMessage("Fail on import file");
    }

}