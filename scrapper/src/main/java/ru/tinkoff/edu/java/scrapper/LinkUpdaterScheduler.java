package ru.tinkoff.edu.java.scrapper;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
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
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
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

    public LinkUpdaterScheduler(LinkUpdater linkUpdater, GitHubClient gitHubClient,
                                BotClient botClient, StackOverflowClient stackOverflowClient) {
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
                                "1 или более коммитов были запушены в репозиторий '".concat(response.fullName()).
                                        concat("'"), chats.stream().map(Chat::id).toList()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else if (object instanceof StackOverflowQuestion question) {
                boolean scenario = false;
                QuestionResponse response = stackOverflowClient.fetchQuestion(question);
                var answers = response.answersTime();
                int countNewAnswers = 0;
                for (var answer : answers) {
                    if (answer.compareTo(link.checkedAt()) > -1) {
                        countNewAnswers++;
                    }
                }
                if (countNewAnswers > 0) {
                    try {
                        botClient.update(new LinkUpdate(link.id(), new URI(link.url()), "На вопрос '".
                                concat(response.title()).concat("' ответили ").concat(String.valueOf(countNewAnswers)).
                                concat(getCorrectForm(countNewAnswers)), chats.stream().map(Chat::id).toList()));
                        scenario = true;
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (response.closedAt() != null && response.closedAt().compareTo(link.checkedAt()) > -1) {
                    try {
                        botClient.update(new LinkUpdate(link.id(), new URI(link.url()),
                                "Вопрос '".concat(response.title()).concat("' был закрыт "),
                                chats.stream().map(Chat::id).toList()));
                        scenario = true;
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (response.updatedAt().compareTo(link.checkedAt()) > -1 && !scenario) {
                    try {
                        botClient.update(new LinkUpdate(link.id(), new URI(link.url()),
                                "Произошло обновление вопроса '".concat(response.title()).concat("'"),
                                chats.stream().map(Chat::id).toList()));
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        linkUpdater.updateAll(updates.stream().map(Subscription::link).distinct().collect(Collectors.toList()));
    }

    private String getCorrectForm(int x) {
        if (x % 10 >= 2 && x % 10 <= 4 && (x % 100 > 14 || x % 100 < 12)) {
            return " раза";
        }
        return " раз";
    }
}
