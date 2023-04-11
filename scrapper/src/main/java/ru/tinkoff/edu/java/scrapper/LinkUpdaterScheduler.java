package ru.tinkoff.edu.java.scrapper;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;
import ru.tinkoff.edu.java.link_parser.GitHubRepository;
import ru.tinkoff.edu.java.link_parser.LinkParser;
import ru.tinkoff.edu.java.link_parser.ParsedObject;
import ru.tinkoff.edu.java.link_parser.StackOverflowQuestion;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.dto.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.dto.RepositoryResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

@Component
public class LinkUpdaterScheduler {
    private final LinkUpdater linkUpdater;
    private final GitHubClient gitHubClient;
    private final BotClient botClient;
    private final StackOverflowClient stackOverflowClient;

    public LinkUpdaterScheduler(LinkUpdater linkUpdater, GitHubClient gitHubClient, BotClient botClient, StackOverflowClient stackOverflowClient) {
        this.linkUpdater = linkUpdater;
        this.gitHubClient = gitHubClient;
        this.botClient = botClient;
        this.stackOverflowClient = stackOverflowClient;
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
            if (object instanceof GitHubRepository repo) {
                RepositoryResponse response = gitHubClient.fetchRepository(repo);
                if (response.pushedAt().compareTo(link.checkedAt()) > -1) {
                    try {
                        botClient.update(new LinkUpdate(link.id(), new URI(link.url()),
                                "Репозиторий обновился", chats.stream().map(Chat::id).toList()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (object instanceof StackOverflowQuestion question) {
                QuestionResponse response = stackOverflowClient.fetchQuestion(question);
                if (response.updatedAt().compareTo(link.checkedAt()) > -1) {
                    try {
                        botClient.update(new LinkUpdate(link.id(), new URI(link.url()),
                                "Вопрос обновился", chats.stream().map(Chat::id).toList()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        linkUpdater.updateAll(updates.stream().map(Subscription::link).distinct().collect(Collectors.toList()));
    }
}
