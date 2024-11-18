package br.com.fiap.product_import.gateway;

import br.com.fiap.product_import.domain.model.ImportFile;

public interface FileGateway {

    void save(ImportFile importFile);

}
