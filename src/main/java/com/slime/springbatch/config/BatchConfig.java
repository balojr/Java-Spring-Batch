package com.slime.springbatch.config;

import com.slime.springbatch.domain.Coffee;
import com.slime.springbatch.listener.JobCompletionNotificationListener;
import com.slime.springbatch.processor.CoffeeItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * This class provides the configuration for the Spring Batch job.
 */
@Configuration
public class BatchConfig {

    /**
     * The file input path.
     */
    @Value("${file.input}")
    private String fileInput;

    /**
     * Configures the reader for the Spring Batch job.
     * @return a FlatFileItemReader object.
     */
    @Bean
    public FlatFileItemReader reader() {
        return new FlatFileItemReaderBuilder().name("coffeeItemReader")
                .resource(new ClassPathResource(fileInput))
                .delimited()
                .names(new String[] { "brand", "origin", "characteristics" })
                .fieldSetMapper(new BeanWrapperFieldSetMapper() {{
                    setTargetType(Coffee.class);
                }})
                .build();
    }

    /**
     * Configures the writer for the Spring Batch job.
     * @param dataSource the DataSource object.
     * @return a JdbcBatchItemWriter object.
     */
    @Bean
    public JdbcBatchItemWriter writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO coffee (brand, origin, characteristics) VALUES (:brand, :origin, :characteristics)")
                .dataSource(dataSource)
                .build();
    }

    /**
     * Configures the job for the Spring Batch job.
     * @param jobRepository the JobRepository object.
     * @param listener the JobCompletionNotificationListener object.
     * @param step1 the Step object.
     * @return a Job object.
     */
    @Bean
    public Job importUserJob(JobRepository jobRepository, JobCompletionNotificationListener listener, Step step1) {
        return new JobBuilder("importUserJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    /**
     * Configures the step for the Spring Batch job.
     * @param jobRepository the JobRepository object.
     * @param transactionManager the PlatformTransactionManager object.
     * @param writer the JdbcBatchItemWriter object.
     * @return a Step object.
     */
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, JdbcBatchItemWriter writer) {
        return new StepBuilder("step1", jobRepository)
                .<Coffee, Coffee> chunk(10, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();
    }

    /**
     * Configures the processor for the Spring Batch job.
     * @return a CoffeeItemProcessor object.
     */
    @Bean
    public CoffeeItemProcessor processor() {
        return new CoffeeItemProcessor();
    }
}