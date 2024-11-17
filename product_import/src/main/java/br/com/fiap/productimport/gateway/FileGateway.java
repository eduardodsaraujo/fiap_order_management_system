package br.com.fiap.productimport.gateway;

import br.com.fiap.productimport.domain.model.ImportFile;

public interface FileGateway {

    void save(ImportFile importFile);

}
