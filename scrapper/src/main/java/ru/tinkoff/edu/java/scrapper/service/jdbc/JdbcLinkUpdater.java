package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

import java.util.List;

@Service
public class JdbcLinkUpdater implements LinkUpdater {
    private final JdbcLinkRepository linkRepository;
    private final JdbcChatLinkRepository chatLinkRepository;

    public JdbcLinkUpdater(JdbcLinkRepository linkRepository, JdbcChatLinkRepository chatLinkRepository) {
        this.linkRepository = linkRepository;
        this.chatLinkRepository = chatLinkRepository;
    }

    @Override
    public List<Subscription> getUpdates() {
        var list = linkRepository.findUnchecked();
        return list.stream().flatMap(link -> chatLinkRepository.findAllByLinkId(link.id()).stream()).toList();
    }

    @Override
    public void updateAll(List<Link> links) {
        links.forEach(linkRepository::updateTime);
    }
}
