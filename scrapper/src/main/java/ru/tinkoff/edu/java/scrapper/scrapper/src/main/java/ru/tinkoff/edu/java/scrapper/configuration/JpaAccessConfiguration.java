package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkUpdater;

@Configuration
@Profile("!test")
@ConditionalOnProperty(prefix = "scrapper", name = "database-access-type", havingValue = "jpa")
public class JpaAccessConfiguration {
    @Bean
    public LinkService linkService(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        return new JpaLinkService(linkRepository, chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(JpaLinkRepository linkRepository, long updateInterval) {
        return new JpaLinkUpdater(linkRepository, updateInterval);
    }

    @Bean
    public ChatService chatService(JpaChatRepository chatRepository) {
        return new JpaChatService(chatRepository);
    }
}