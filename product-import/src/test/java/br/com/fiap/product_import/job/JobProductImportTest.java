package br.com.fiap.product_import.job;


import br.com.fiap.product_import.ProductImportApplication;
import br.com.fiap.product_import.domain.model.ImportFile;
import br.com.fiap.product_import.gateway.filesystem.FileSystemGateway;
import br.com.fiap.product_import.utils.FilesUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ActiveProfiles("test")
@SpringBootTest(classes = {ProductImportApplication.class})
@SpringBatchTest
public class JobProductImportTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Value("${import-file.input-path}")
    private String directory;

    private FileSystemGateway fileSystemGateway;

    @BeforeEach
    public void setup() throws IOException {
        fileSystemGateway = new FileSystemGateway();
        FilesUtils.deleteDirectory(new File(directory));
    }

    @Test
    public void shouldExecuteJob(@Autowired Job job) throws Exception {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory(directory)
                .bytes("IP14;iphone 14;iphone 14 128;Smartphone;Apple;true;9000;2;20".getBytes())
                .build();
        fileSystemGateway.save(importFile);

        this.jobLauncherTestUtils.setJob(job);

        // Act
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Assert
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }

    @Test
    public void shouldExecuteInitialStep(@Autowired Job job) {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory(directory)
                .bytes("IP18;iphone 18;iphone 18 128;Smartphone;Apple;true;9000;2;20".getBytes())
                .build();
        fileSystemGateway.save(importFile);

        this.jobLauncherTestUtils.setJob(job);

        // Act
        JobExecution jobExecution = this.jobLauncherTestUtils.launchStep("initial-step");

        // Assert
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }

    @Test
    public void shouldExecuteMoveFilesStep(@Autowired Job job) {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory(directory)
                .bytes("IP14;iphone 14;iphone 14 128;Smartphone;Apple;true;9000;2;20".getBytes())
                .build();
        fileSystemGateway.save(importFile);

        this.jobLauncherTestUtils.setJob(job);

        // Act
        JobExecution jobExecution = this.jobLauncherTestUtils.launchStep("move-files");

        // Assert
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }

    @Test
    public void shouldFail_WhenExecuteJob_WithDifferentPath(@Autowired Job job) throws Exception {
        // Arrange
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory(directory + "/files2")
                .bytes("IP14;iphone 14;iphone 14 128;Smartphone;Apple;true;9000;2;20".getBytes())
                .build();
        fileSystemGateway.save(importFile);

        this.jobLauncherTestUtils.setJob(job);

        // Act
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Assert

        assertEquals("FAILED", jobExecution.getExitStatus().getExitCode());
    }

    @Test
    public void shouldFail_WhenExecuteMoveFilesStep_WithoutPermission(@Autowired Job job) throws Exception {
        ImportFile importFile = ImportFile.builder()
                .name("data.csv")
                .directory(directory)
                .bytes("IP18;iphone 18;iphone 18 128;Smartphone;Apple;true;9000;2;20".getBytes())
                .build();
        fileSystemGateway.save(importFile);

        File file = new File(directory);
        file.setWritable(false, false);

        // Arrange
        this.jobLauncherTestUtils.setJob(job);

        // Act
        JobExecution jobExecution = this.jobLauncherTestUtils.launchStep("move-files");

        // Assert
        assertEquals("FAILED", jobExecution.getExitStatus().getExitCode());
        file.setWritable(true);
    }

}
