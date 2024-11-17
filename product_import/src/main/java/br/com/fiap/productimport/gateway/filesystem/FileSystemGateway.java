package br.com.fiap.productimport.gateway.filesystem;

import br.com.fiap.productimport.domain.model.ImportFile;
import br.com.fiap.productimport.gateway.FileGateway;
import br.com.fiap.productimport.infra.exception.ProductImportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
public class FileSystemGateway implements FileGateway {

    @Override
    public void save(ImportFile importFile) {
        try {
            Path filePath = Paths.get(importFile.getDirectory(), importFile.getName());
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, importFile.getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ProductImportException("Fail on import file");
        }
    }

}
