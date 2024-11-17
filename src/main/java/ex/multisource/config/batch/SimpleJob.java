package ex.multisource.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j(topic = "simple-job")
@Configuration
@RequiredArgsConstructor
public class SimpleJob {

    private static final String JOB_NAME = "simple_job";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean(JOB_NAME)
    public Job simpleJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
            .start(simpleStep())
            .incrementer(new RunIdIncrementer())
            .build();
    }

    @Bean(JOB_NAME + "_step")
    public Step simpleStep() {
        return new StepBuilder(JOB_NAME + "_step", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                log.info("Hello, world!");
                return null;
            }, transactionManager)
            .build();
    }
}
