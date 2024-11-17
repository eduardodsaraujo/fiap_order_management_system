package br.com.fiap.productimport.application.service;

import br.com.fiap.productimport.domain.model.ImportFile;
import br.com.fiap.productimport.gateway.FileGateway;
import br.com.fiap.productimport.gateway.JobGateway;
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
