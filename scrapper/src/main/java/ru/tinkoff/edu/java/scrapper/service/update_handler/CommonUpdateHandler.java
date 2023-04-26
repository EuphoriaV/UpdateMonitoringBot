package ru.tinkoff.edu.java.scrapper.service.update_handler;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.link_parser.GitHubRepository;
import ru.tinkoff.edu.java.link_parser.ParsedObject;
import ru.tinkoff.edu.java.link_parser.StackOverflowQuestion;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;

import java.util.List;

@Service
public class CommonUpdateHandler implements UpdateHandler {
    private final GitHubUpdateHandler gitHubUpdateHandler;
    private final StackOverflowUpdateHandler stackOverflowUpdateHandler;

    public CommonUpdateHandler(GitHubUpdateHandler gitHubUpdateHandler, StackOverflowUpdateHandler stackOverflowUpdateHandler) {
        this.gitHubUpdateHandler = gitHubUpdateHandler;
        this.stackOverflowUpdateHandler = stackOverflowUpdateHandler;
    }

    @Override
    public void handleUpdate(ParsedObject object, Link link, List<Chat> chats) {
        if (object instanceof GitHubRepository repo) {
            gitHubUpdateHandler.handleUpdate(repo, link, chats);
        } else if (object instanceof StackOverflowQuestion question) {
            stackOverflowUpdateHandler.handleUpdate(question, link, chats);
        }
    }
}
