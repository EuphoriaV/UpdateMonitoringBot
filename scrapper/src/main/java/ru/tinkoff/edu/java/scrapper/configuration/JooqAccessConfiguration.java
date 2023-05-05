package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkUpdater;

@Configuration
@Profile("!test")
@ConditionalOnProperty(prefix = "scrapper", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public LinkService linkService(
        JooqChatLinkRepository chatLinkRepository,
        JooqLinkRepository linkRepository,
        JooqChatRepository chatRepository
    ) {
        return new JooqLinkService(chatLinkRepository, linkRepository, chatRepository);
    }

    @Bean
    public LinkUpdater linkUpdater(JooqLinkRepository linkRepository, JooqChatLinkRepository chatLinkRepository) {
        return new JooqLinkUpdater(linkRepository, chatLinkRepository);
    }

    @Bean
    public ChatService chatService(JooqChatRepository chatRepository) {
        return new JooqChatService(chatRepository);
    }
}
