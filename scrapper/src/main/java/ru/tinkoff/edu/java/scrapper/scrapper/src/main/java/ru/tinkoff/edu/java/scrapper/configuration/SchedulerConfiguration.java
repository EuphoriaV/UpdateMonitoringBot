package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerConfiguration {
    @Bean
    public long schedulerInterval(ApplicationConfig config) {
        return config.scheduler().interval().toMillis();
    }

    @Bean
    public long updateInterval(ApplicationConfig config) {
        return config.updateInterval();
    }
}
