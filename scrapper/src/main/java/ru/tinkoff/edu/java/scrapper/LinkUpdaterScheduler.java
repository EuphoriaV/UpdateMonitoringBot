package ru.tinkoff.edu.java.scrapper;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.link_parser.LinkParser;
import ru.tinkoff.edu.java.link_parser.ParsedObject;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.update_handler.CommonUpdateHandler;

import java.util.stream.Collectors;

@Component
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;
    private final CommonUpdateHandler commonUpdateHandler;

    public LinkUpdaterScheduler(LinkUpdater linkUpdater, CommonUpdateHandler commonUpdateHandler) {
        this.linkUpdater = linkUpdater;
        this.commonUpdateHandler = commonUpdateHandler;
    }

    @Scheduled(fixedDelayString = "#{schedulerInterval}")
    public void update() {
        var updates = linkUpdater.getUpdates();
        var map = updates.stream().collect(Collectors.groupingBy(Subscription::link,
                Collectors.mapping(Subscription::chat, Collectors.toList())));
        for (var entry : map.entrySet()) {
            Link link = entry.getKey();
            var chats = entry.getValue();
            ParsedObject object = LinkParser.parseLink(link.url());
            commonUpdateHandler.handleUpdate(object, link, chats);
        }
        linkUpdater.updateAll(updates.stream().map(Subscription::link).distinct().collect(Collectors.toList()));
    }
}
