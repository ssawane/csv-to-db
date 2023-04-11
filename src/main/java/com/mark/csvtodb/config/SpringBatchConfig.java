package com.mark.csvtodb.config;

import com.mark.csvtodb.model.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class SpringBatchConfig {
    @Value("file:src/main/resources/input/*.csv")
    private final Resource[] inputResources;

    @Bean
    public Job importJob(JobRepository jobRepository, Step stepOne) {
        return new JobBuilder("importJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(stepOne)
                .end()
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }

    @Bean
    public Step stepOne(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                        ItemWriter<Profile> itemWriter, ItemProcessor<Profile, Profile> itemProcessor) {
        return new StepBuilder("stepOne", jobRepository)
                .<Profile, Profile> chunk(1000, transactionManager)
                .reader(multiResourceItemReader())
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public MultiResourceItemReader<Profile> multiResourceItemReader() {
        MultiResourceItemReader<Profile> resourceItemReader = new MultiResourceItemReader<>();
        resourceItemReader.setResources(inputResources);
        resourceItemReader.setDelegate(reader());
        return  resourceItemReader;
    }

    @Bean
    public FlatFileItemReader<Profile> reader() {
        return new FlatFileItemReaderBuilder<Profile>()
                .name("profileReader")
                .linesToSkip(1)
                .delimited()
                .names("name", "surname", "email")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<Profile>() {{
                    setTargetType(Profile.class);
                }})
                .build();
    }
}
