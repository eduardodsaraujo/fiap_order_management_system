package br.com.fiap.product_import.application.service;

import br.com.fiap.product_import.domain.model.ImportFile;
import br.com.fiap.product_import.gateway.FileGateway;
import br.com.fiap.product_import.gateway.JobGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductImportService {

    private final FileGateway fileGateway;
    private final JobGateway jobGateway;

    public void execute(ImportFile importFile) {
        fileGateway.save(importFile);
        jobGateway.execute(importFile);
    }

}
