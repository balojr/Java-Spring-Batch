package com.slime.springbatch.listener;

import com.slime.springbatch.domain.Coffee;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the JobExecutionListener interface provided by Spring Batch.
 * It listens for when a job is completed and then logs the results.
 */
@Slf4j
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * This method is called after a job execution.
     * If the job is completed, it logs the results by querying the coffee table in the database.
     *
     * @param jobExecution the JobExecution object that contains information about the current job
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.error("JOB FINISHED! Time to verify the results");

            String query = "SELECT brand, origin, characteristics FROM coffee";
            jdbcTemplate.query(query, (rs, row) -> new Coffee(rs.getString(1), rs.getString(2), rs.getString(3)))
                    .forEach(coffee -> log.info("Found < {} > in the database.", coffee));
        }
    }
}
