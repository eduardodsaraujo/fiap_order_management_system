package br.com.fiap.productimport.gateway.springbatch;

import br.com.fiap.productimport.domain.model.ImportFile;
import br.com.fiap.productimport.gateway.JobGateway;
import br.com.fiap.productimport.infra.exception.ProductImportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringBatchGateway implements JobGateway {

    private final JobLauncher jobLauncher;
    private final Job job;

    public SpringBatchGateway(JobLauncher jobLauncher, @Qualifier("job") Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @Override
    public void execute(ImportFile importFile) {
        try {
            JobParameters jobParameters = new JobParameters();
            jobLauncher.run(job, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ProductImportException("Failed to process Job");
        }
    }

}
