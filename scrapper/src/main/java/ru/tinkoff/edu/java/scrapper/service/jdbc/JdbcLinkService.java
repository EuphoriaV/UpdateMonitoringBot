package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractLinkService;

@Service
@Primary
public class JdbcLinkService extends AbstractLinkService {
    public JdbcLinkService(JdbcChatLinkRepository chatLinkRepository,
                           JdbcLinkRepository linkRepository, JdbcChatRepository chatRepository) {
        this.chatLinkRepository = chatLinkRepository;
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }
}
