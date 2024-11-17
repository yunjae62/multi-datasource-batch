package ex.multisource.config.batch;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Launcher {

    private final Job simpleJob;
    private final JobLauncher jobLauncher;

    @Scheduled(cron = "0 0 0/1 * * *", zone = "Asia/Seoul")
    public void run() {
        JobParameters jobParameters = new JobParameters(
            Map.of("requestTime", new JobParameter<>(System.currentTimeMillis(), Long.class))
        );

        try {
            jobLauncher.run(simpleJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException
                 | JobRestartException e) {
            throw new RuntimeException(e);
        }
    }
}
