package ru.tinkoff.edu.java.scrapper.service.jooq;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractChatService;

@Service
@Primary
public class JooqChatService extends AbstractChatService {
    public JooqChatService(JooqChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }
}
