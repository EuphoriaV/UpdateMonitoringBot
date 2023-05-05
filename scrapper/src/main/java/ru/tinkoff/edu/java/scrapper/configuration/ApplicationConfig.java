package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.AccessType;
import ru.tinkoff.edu.java.scrapper.dto.Scheduler;

@Validated
@ConfigurationProperties(prefix = "scrapper", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull Scheduler scheduler, @NotNull Long updateInterval,
                                @NotNull AccessType databaseAccessType, @NotNull String queueName,
                                @NotNull String exchangeName, boolean useQueue) {
}
