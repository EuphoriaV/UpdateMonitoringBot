package ru.tinkoff.edu.java.scrapper;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LinkUpdaterScheduler {
    @Scheduled(fixedDelayString = "#{schedulerInterval}")
    public void update() {
        System.out.println("Update");
    }
}
