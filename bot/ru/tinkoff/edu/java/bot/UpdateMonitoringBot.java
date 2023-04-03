package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//У бота username @UpdateMonitoringBot (меню добавлено)
@Component
public class UpdateMonitoringBot extends Bot {
    public final TelegramBot telegramBot;

    public UpdateMonitoringBot(@Value("${app.token}") String token) {
        this.telegramBot = new TelegramBot(token);
        telegramBot.setUpdatesListener(this);
    }

    public SendResponse start(long chatId) {
        return sendMessage(chatId, "Registering user...");
    }

    public SendResponse list(long chatId) {
        return sendMessage(chatId, "Пока что отсутствуют отслеживаемые ссылки");
    }

    public SendResponse help(long chatId) {
        return sendMessage(chatId, """
                /start - зарегистрировать пользователя
                /help - вывести окно с командами
                /track - начать отслеживание ссылки
                /untrack - прекратить отслеживание ссылки
                /list - показать список отслеживаемых ссылок""");
    }

    public SendResponse track(long chatId) {
        return sendMessage(chatId, "Tracking link...");
    }

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
