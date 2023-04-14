package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractChatService;

@Service
public class JdbcChatService extends AbstractChatService {
    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
}
