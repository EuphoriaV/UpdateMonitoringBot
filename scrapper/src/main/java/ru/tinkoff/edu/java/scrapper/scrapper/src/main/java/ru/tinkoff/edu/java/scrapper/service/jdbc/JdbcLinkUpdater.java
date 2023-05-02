package ru.tinkoff.edu.java.scrapper.service.jdbc;

import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractLinkUpdater;

public class JdbcLinkUpdater extends AbstractLinkUpdater {
    public JdbcLinkUpdater(JdbcLinkRepository linkRepository, JdbcChatLinkRepository chatLinkRepository) {
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
    }
}
