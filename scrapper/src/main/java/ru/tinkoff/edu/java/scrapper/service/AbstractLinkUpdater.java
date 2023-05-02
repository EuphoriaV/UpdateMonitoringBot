package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.LinkRepository;

import java.util.List;

public class AbstractLinkUpdater implements LinkUpdater {
    protected LinkRepository linkRepository;
    protected ChatLinkRepository chatLinkRepository;

    public List<Subscription> getUpdates() {
        var list = linkRepository.findUnchecked();
        return list.stream().flatMap(link -> chatLinkRepository.findAllByLinkId(link.id()).stream()).toList();
    }

    public void updateAll(List<Link> links) {
        links.forEach(linkRepository::updateTime);
    }
}
