package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.annotation.Command;

import java.lang.reflect.Method;

//У бота username @UpdateMonitoringBot (меню добавлено)
@Component
public class UpdateMonitoringBot extends Bot {
    public final TelegramBot telegramBot;

    public UpdateMonitoringBot(@Value("${bot.token}") String token) {
        this.telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(this);
    }

    @Command(name = "/help", description = "вывести окно с командами")
    public SendResponse help(long chatId) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Method method : getCommandHandlers()) {
            var command = method.getAnnotation(Command.class);
            stringBuilder.append(command.name()).append(" - ").append(command.description()).append("\n");
        }
        return sendMessage(chatId, stringBuilder.toString());
    }

    @Command(name = "/start", description = "зарегистрировать пользователя")
    public SendResponse start(long chatId) {
        return sendMessage(chatId, "Registering user...");
    }

    @Command(name = "/list", description = "показать список отслеживаемых ссылок")
    public SendResponse list(long chatId) {
        return sendMessage(chatId, "Пока что отсутствуют отслеживаемые ссылки");
    }

    @Command(name = "/track", description = "начать отслеживание ссылки")
    public SendResponse track(long chatId) {
        return sendMessage(chatId, "Tracking link...");
    }

    @Command(name = "/untrack", description = "прекратить отслеживание ссылки")
    public SendResponse untrack(long chatId) {
        return sendMessage(chatId, "Untracking link...");
    }

    @Override
    public SendResponse handleInvalidMessage(long chatId, String text) {
        return sendMessage(chatId, "Неизвестная команда: " + text);
    }

    private SendResponse sendMessage(long chatId, String text) {
        SendMessage sendMessage = new SendMessage(chatId, "<i>".concat(text).concat("</i>")).
                parseMode(ParseMode.HTML);
        return telegramBot.execute(sendMessage);
    }
}
