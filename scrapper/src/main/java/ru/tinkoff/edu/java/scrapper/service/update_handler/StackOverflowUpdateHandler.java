package ru.tinkoff.edu.java.scrapper.service.update_handler;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.link_parser.ParsedObject;
import ru.tinkoff.edu.java.link_parser.StackOverflowQuestion;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.dto.QuestionResponse;
import ru.tinkoff.edu.java.scrapper.service.MessageService;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
public class StackOverflowUpdateHandler implements UpdateHandler {
    private final MessageService messageService;
    private final StackOverflowClient stackOverflowClient;

    public StackOverflowUpdateHandler(MessageService messageService, StackOverflowClient stackOverflowClient) {
        this.messageService = messageService;
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public void handleUpdate(ParsedObject object, Link link, List<Chat> chats) {
        if (object instanceof StackOverflowQuestion question) {
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
                    messageService.sendMessage(new LinkUpdate(link.id(), new URI(link.url()), "На вопрос '"
                        .concat(response.title()).concat("' ответили ").concat(String.valueOf(countNewAnswers))
                        .concat(getCorrectForm(countNewAnswers)), chats.stream().map(Chat::id).toList()));
                    scenario = true;
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            if (response.closedAt() != null && response.closedAt().compareTo(link.checkedAt()) > -1) {
                try {
                    messageService.sendMessage(new LinkUpdate(link.id(), new URI(link.url()),
                        "Вопрос '".concat(response.title()).concat("' был закрыт "),
                        chats.stream().map(Chat::id).toList()
                    ));
                    scenario = true;
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
            if (response.updatedAt().compareTo(link.checkedAt()) > -1 && !scenario) {
                try {
                    messageService.sendMessage(new LinkUpdate(link.id(), new URI(link.url()),
                        "Произошло обновление вопроса '".concat(response.title()).concat("'"),
                        chats.stream().map(Chat::id).toList()
                    ));
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private String getCorrectForm(int x) {
        if (x % 10 >= 2 && x % 10 <= 4 && (x % 100 > 14 || x % 100 < 12)) {
            return " раза";
        }
        return " раз";
    }
}
