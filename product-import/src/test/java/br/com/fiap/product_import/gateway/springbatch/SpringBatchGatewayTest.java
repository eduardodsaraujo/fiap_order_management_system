package br.com.fiap.product_import.gateway.springbatch;

import br.com.fiap.product_import.domain.model.ImportFile;
import br.com.fiap.product_import.infra.exception.ProductImportException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SpringBatchGatewayTest {

    private SpringBatchGateway springBatchGateway;

    @Mock
    private JobLauncher jobLauncher;
    @Mock
    private Job job;

    private AutoCloseable openMocks;

    @BeforeEach
    public void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        springBatchGateway = new SpringBatchGateway(jobLauncher, job);
    }

    @AfterEach
    public void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    public void shouldExecuteJob() throws Exception {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory("./files/")
                .bytes(new byte[]{1, 2, 3})
                .build();

        when(jobLauncher.run(any(Job.class),any(JobParameters.class))).thenReturn(new JobExecution(1L));

        // Act
        springBatchGateway.execute(importFile);

        // Assert
        verify(jobLauncher, times(1)).run(any(Job.class),any(JobParameters.class));
    }

    @Test
    public void shouldThrowException_WhenExecuteJob_WithNullJob() throws Exception {
        // Arrange
        // Act
        when(jobLauncher.run(any(Job.class),any(JobParameters.class))).thenThrow(new ProductImportException(""));
        // Assert
        assertThatThrownBy(() -> springBatchGateway.execute(null))
                .isInstanceOf(ProductImportException.class)
                .hasMessage("Failed to process Job");
    }

}