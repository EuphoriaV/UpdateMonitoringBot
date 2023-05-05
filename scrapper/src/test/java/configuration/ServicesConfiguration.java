package configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkUpdater;

@TestConfiguration
@Profile("test")
public class ServicesConfiguration {
    @Bean
    @Primary
    public JpaLinkService jpaLinkService(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        return new JpaLinkService(linkRepository, chatRepository);
    }

    @Bean
    @Primary
    public JpaLinkUpdater jpaLinkUpdater(JpaLinkRepository linkRepository, long updateInterval) {
        return new JpaLinkUpdater(linkRepository, updateInterval);
    }

    @Bean
    @Primary
    public JpaChatService jpaChatService(JpaChatRepository chatRepository) {
        return new JpaChatService(chatRepository);
    }

    @Bean
    public JdbcLinkService jdbcLinkService(JdbcChatLinkRepository chatLinkRepository,
                                           JdbcLinkRepository linkRepository, JdbcChatRepository chatRepository) {
        return new JdbcLinkService(chatLinkRepository, linkRepository, chatRepository);
    }

    @Bean
    public JdbcLinkUpdater jdbcLinkUpdater(JdbcLinkRepository linkRepository,
                                           JdbcChatLinkRepository chatLinkRepository) {
        return new JdbcLinkUpdater(linkRepository, chatLinkRepository);
    }

    @Bean
    public JdbcChatService jdbcChatService(JdbcChatRepository chatRepository) {
        return new JdbcChatService(chatRepository);
    }

    @Bean
    public JooqLinkService jooqLinkService(JooqChatLinkRepository chatLinkRepository,
                                           JooqLinkRepository linkRepository, JooqChatRepository chatRepository) {
        return new JooqLinkService(chatLinkRepository, linkRepository, chatRepository);
    }

    @Bean
    public JooqLinkUpdater jooqLinkUpdater(JooqLinkRepository linkRepository,
                                           JooqChatLinkRepository chatLinkRepository) {
        return new JooqLinkUpdater(linkRepository, chatLinkRepository);
    }

    @Bean
    public JooqChatService jooqChatService(JooqChatRepository chatRepository) {
        return new JooqChatService(chatRepository);
    }
}
