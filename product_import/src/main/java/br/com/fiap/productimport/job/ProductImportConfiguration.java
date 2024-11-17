package br.com.fiap.productimport.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;
import java.io.File;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProductImportConfiguration {

    private final PlatformTransactionManager transactionManager;

    @Value("${import-file.input-path}")
    private String directory;

    @Bean
    public Job job(
            @Qualifier("initialStep") Step inicialStep,
            @Qualifier("moveFilesStep") Step moveFilesStep,
            JobRepository jobRepository) {
        return new JobBuilder("import-files", jobRepository)
                .start(inicialStep)
                .next(moveFilesStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step initialStep(
            @Qualifier("reader") ItemReader<ProductImport> reader,
            @Qualifier("writer") ItemWriter<ProductImport> writer,
            @Qualifier("processor") ProductImportProcessor processor,
            JobRepository jobRepository) {
        return new StepBuilder("inicial-step", jobRepository)
                .<ProductImport, ProductImport>chunk(200, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public ItemReader<ProductImport> reader() {
        return new FlatFileItemReaderBuilder<ProductImport>()
                .name("read-csv")
                .resource(new FileSystemResource(directory + "/data.csv"))
                .comments("--")
                .delimited()
                .delimiter(";")
                .names("code", "name", "description", "category", "manufacturer", "enable", "price", "weight", "stockQuantity")
                .fieldSetMapper(new ProductImportMapper())
                .build();
    }

    @Bean
    public ItemWriter<ProductImport> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ProductImport>()
                .dataSource(dataSource)
                .sql("""
                        insert into product (code, name, description, category, manufacturer, enable, price, weight, stock_quantity)
                        values (:code, :name, :description, :category, :manufacturer, :enable, :price, :weight, :stockQuantity)""")
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .build();
    }

    @Bean
    public ProductImportProcessor processor() {
        return new ProductImportProcessor();
    }

    @Bean
    public Tasklet moveFileTasklet() {
        return (contribution, chunkContext) -> {
            File sourceFolder = new File(directory);
            File destinationFolder = new File(directory + "/done");

            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            File[] files = sourceFolder.listFiles((dir, name) -> name.endsWith(".csv"));

            if (files != null) {
                for (File file : files) {
                    File destinationFile = new File(destinationFolder, file.getName());
                    if (file.renameTo(destinationFile)) {
                        log.info("File moved: {}", file.getName());
                    } else {
                        log.warn("Could not move file: {}", file.getName());
                        throw new RuntimeException("Could not move file: " + file.getName());
                    }
                }
            }
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step moveFilesStep(JobRepository jobRepository) {
        return new StepBuilder("move-file", jobRepository)
                .tasklet(moveFileTasklet(), transactionManager)
                .allowStartIfComplete(true)
                .build();
    }


}
