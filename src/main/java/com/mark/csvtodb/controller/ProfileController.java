package com.mark.csvtodb.controller;

import com.mark.csvtodb.model.Profile;
import com.mark.csvtodb.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ProfileController {
    private final JobLauncher jobLauncher;
    private final ProfileService profileService;
    private final Job job;


    @GetMapping("/load")
    public BatchStatus load() {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLong("time", System.currentTimeMillis());

        JobExecution jobExecution = null;
        try {
            jobExecution = jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException |
                 JobParametersInvalidException e) {
            e.printStackTrace();
        }

        if (jobExecution != null) {
            System.out.println("JobExecution: " + jobExecution.getStatus());
            return jobExecution.getStatus();
        }

        return BatchStatus.FAILED;
    }

    @GetMapping("/test")
    public Profile getProfile(@RequestParam(value = "email") String email) {
        Optional<Profile> foundProfile = profileService.findByEmail(email);
        return foundProfile.orElseGet(Profile::new);
    }
}
