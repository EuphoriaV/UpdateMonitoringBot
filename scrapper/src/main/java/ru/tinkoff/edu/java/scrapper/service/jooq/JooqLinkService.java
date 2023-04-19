package ru.tinkoff.edu.java.scrapper.service.jooq;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractLinkService;

@Service
public class JooqLinkService extends AbstractLinkService {
    public JooqLinkService(JooqChatLinkRepository chatLinkRepository,
                           JooqLinkRepository linkRepository, JooqChatRepository chatRepository) {
        this.chatLinkRepository = chatLinkRepository;
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }
}
