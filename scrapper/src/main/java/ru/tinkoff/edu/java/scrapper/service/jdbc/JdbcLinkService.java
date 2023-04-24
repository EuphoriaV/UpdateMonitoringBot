package ru.tinkoff.edu.java.scrapper.service.jdbc;

import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractLinkService;

public class JdbcLinkService extends AbstractLinkService {
    public JdbcLinkService(JdbcChatLinkRepository chatLinkRepository,
                           JdbcLinkRepository linkRepository, JdbcChatRepository chatRepository) {
        this.chatLinkRepository = chatLinkRepository;
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }
}
