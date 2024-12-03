package br.com.fiap.product_import.gateway.springbatch;

import br.com.fiap.product_import.domain.model.ImportFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringBatchGatewayIT {

    @Autowired
    private SpringBatchGateway springBatchGateway;

    @Test
    public void shouldExecuteJob() {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory("./files/")
                .bytes(new byte[]{1, 2, 3})
                .build();

        // Act
        springBatchGateway.execute(importFile);
    }

}