package ru.tinkoff.edu.java.scrapper.service.jdbc;

import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractChatService;

public class JdbcChatService extends AbstractChatService {
    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
}
