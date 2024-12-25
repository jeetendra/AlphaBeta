package com.jeet.schedular.service;

import com.jeet.schedular.utils.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventTrigger {

    private final AppProperties appProperties;

    @Scheduled(initialDelay = 1000, fixedRate = 5000)
    public void trigger() {
        System.out.println("trigger"+appProperties.getName());
    }

    // second minute hour day month weekday [year]
    @Scheduled(cron = "0 * * * * *")
    public void triggerCron() {
        System.out.println("triggerCron"+appProperties.getName());
    }
}
