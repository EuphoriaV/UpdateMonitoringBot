package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.annotation.Command;
import ru.tinkoff.edu.java.link_parser.LinkParser;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.RemoveLinkRequest;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

//У бота username @UpdateMonitoringBot (меню добавлено)
@Component
public class UpdateMonitoringBot extends Bot {
    private final TelegramBot telegramBot;
    private final ScrapperClient scrapperClient;

    public UpdateMonitoringBot(@Value("${bot.token}") String token, ScrapperClient scrapperClient) {
        this.telegramBot = new TelegramBot(token);
        this.scrapperClient = scrapperClient;
        telegramBot.setUpdatesListener(this);
    }

    @Command(name = "/help", description = "вывести окно с командами")
    public SendResponse help(Message message) {
        StringBuilder stringBuilder = new StringBuilder();
        long chatId = message.chat().id();
        for (Method method : getCommandHandlers()) {
            var command = method.getAnnotation(Command.class);
            stringBuilder.append(command.name()).append(" - ").append(command.description()).append("\n");
        }
        return sendMessage(chatId, stringBuilder.toString());
    }

    @Command(name = "/start", description = "зарегистрировать пользователя")
    public SendResponse start(Message message) {
        long chatId = message.chat().id();
        scrapperClient.createChat(chatId, message.chat().username());
        return sendMessage(chatId, "Вы успешно зарегистрировались");
    }

    @Command(name = "/list", description = "показать список отслеживаемых ссылок")
    public SendResponse list(Message message) {
        long chatId = message.chat().id();
        var res = scrapperClient.getLinks(chatId);
        if (res == null || res.size() == 0) {
            return sendMessage(chatId, "Пока что отсутствуют отслеживаемые ссылки");
        }
        return sendMessage(chatId, "Отслеживаемые ссылки:\n".concat(res.links().stream().
                map(linkResponse -> linkResponse.url().toString()).collect(Collectors.joining("\n"))));
    }

    @Command(name = "/track", description = "начать отслеживание ссылки")
    public SendResponse track(Message message) {
        long chatId = message.chat().id();
        String[] words = message.text().split("\\s+");
        if (words.length < 2 || words[1].isBlank()) {
            return sendMessage(chatId, "Формат команды должен быть: /track 'ссылка'");
        }
        String url = words[1];
        if (LinkParser.parseLink(url) == null) {
            return sendMessage(chatId, "Ссылка невалидная");
        }
        try {
            scrapperClient.addLink(chatId, new AddLinkRequest(new URI(url)));
            return sendMessage(chatId, "Ссылка теперь отслеживается");
        } catch (URISyntaxException e) {
            return sendMessage(chatId, "Ссылка невалидная");
        }
    }

    @Command(name = "/untrack", description = "прекратить отслеживание ссылки")
    public SendResponse untrack(Message message) {
        long chatId = message.chat().id();
        String[] words = message.text().split("\\s+");
        if (words.length < 2 || words[1].isBlank()) {
            return sendMessage(chatId, "Формат команды должен быть: /untrack 'ссылка'");
        }
        String url = words[1];
        if (LinkParser.parseLink(url) == null) {
            return sendMessage(chatId, "Ссылка невалидная");
        }
        try {
            scrapperClient.deleteLink(chatId, new RemoveLinkRequest(new URI(url)));
            return sendMessage(chatId, "Ссылка теперь не отслеживается");
        } catch (URISyntaxException e) {
            return sendMessage(chatId, "Ссылка невалидная");
        }
    }

    @Override
    public SendResponse handleInvalidMessage(Message message) {
        return sendMessage(message.chat().id(), "Неизвестная команда: " + message.text());
    }

    public SendResponse sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, "<i>".concat(text).concat("</i>")).
                parseMode(ParseMode.HTML);
        return telegramBot.execute(sendMessage);
    }
}
