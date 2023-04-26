package ru.tinkoff.edu.java.scrapper.service.update_handler;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.link_parser.GitHubRepository;
import ru.tinkoff.edu.java.link_parser.ParsedObject;
import ru.tinkoff.edu.java.scrapper.client.GitHubClient;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.dto.RepositoryResponse;
import ru.tinkoff.edu.java.scrapper.service.MessageService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class GitHubUpdateHandler implements UpdateHandler {
    private final GitHubClient gitHubClient;
    private final MessageService messageService;

    public GitHubUpdateHandler(GitHubClient gitHubClient, MessageService messageService) {
        this.gitHubClient = gitHubClient;
        this.messageService = messageService;
    }

    @Override
    public void handleUpdate(ParsedObject object, Link link, List<Chat> chats) {
        if (object instanceof GitHubRepository repo) {
            RepositoryResponse response = gitHubClient.fetchRepository(repo);
            if (response.pushedAt().compareTo(link.checkedAt()) > -1) {
                try {
                    messageService.sendMessage(new LinkUpdate(link.id(), new URI(link.url()),
                            "1 или более коммитов были запушены в репозиторий '".concat(response.fullName()).
                                    concat("'"), chats.stream().map(Chat::id).toList()));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
