package br.com.fiap.productimport.gateway;

import br.com.fiap.productimport.domain.model.ImportFile;

public interface JobGateway {

    void execute(ImportFile importFile);

}
