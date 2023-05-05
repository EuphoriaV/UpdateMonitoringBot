package ru.tinkoff.edu.java.scrapper.service.jpa;

import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.entity.Chat;
import ru.tinkoff.edu.java.scrapper.database.entity.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class JpaLinkUpdater implements LinkUpdater {
    private final JpaLinkRepository jpaLinkRepository;

    private final long updateInterval;

    public JpaLinkUpdater(JpaLinkRepository jpaLinkRepository, long updateInterval) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.updateInterval = updateInterval;
    }

    public List<Subscription> getUpdates() {
        var links = jpaLinkRepository.findAllByCheckedAtIsLessThanEqual(
            OffsetDateTime.now().minusSeconds(updateInterval));
        List<Subscription> res = new ArrayList<>();
        for (Link link : links) {
            for (Chat chat : link.getChats()) {
                res.add(new Subscription(
                    new ru.tinkoff.edu.java.scrapper.database.dto.Chat(chat.getId(), chat.getUsername()),
                    new ru.tinkoff.edu.java.scrapper.database.dto.Link(link.getId(),
                        link.getUrl(), link.getCheckedAt()
                    )
                ));
            }
        }
        return res;
    }

    public void updateAll(List<ru.tinkoff.edu.java.scrapper.database.dto.Link> links) {
        for (var link : links) {
            jpaLinkRepository.updateTime(link.url());
        }
    }
}
