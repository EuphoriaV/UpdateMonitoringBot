package ru.tinkoff.edu.java.scrapper.service.jooq;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.AbstractLinkUpdater;

@Service
public class JooqLinkUpdater extends AbstractLinkUpdater {
    public JooqLinkUpdater(JooqLinkRepository linkRepository, JooqChatLinkRepository chatLinkRepository) {
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
    }
}
