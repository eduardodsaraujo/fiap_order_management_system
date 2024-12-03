package br.com.fiap.product_import.job;


import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@SpringBatchTest
public class ReaderProductImportTest {

    @Value("${import-file.input-path}")
    private String directory;

    @Autowired
    private FlatFileItemReader<ProductImport> reader;

    @Test
    public void testTypeConversion() throws Exception {
        // Arrange
        Path filePath = Paths.get(directory, "data.csv");
        Files.write(filePath, "IP15;iphone 15;iphone 15 128;Smartphone;Apple;true;9000;2;20".getBytes());

        // Act
        // Assert
        this.reader.open(new ExecutionContext());
        assertNotNull(this.reader.read());
    }

}
