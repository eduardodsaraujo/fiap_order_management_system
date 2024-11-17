package br.com.fiap.product_import.gateway;

import br.com.fiap.product_import.domain.model.ImportFile;

public interface JobGateway {

    void execute(ImportFile importFile);

}
