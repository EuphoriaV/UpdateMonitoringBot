package ru.tinkoff.edu.java.bot.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.UpdateMonitoringBot;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

@Service
public class BotService {
    private final UpdateMonitoringBot updateMonitoringBot;

    public BotService(UpdateMonitoringBot updateMonitoringBot) {
        this.updateMonitoringBot = updateMonitoringBot;
    }

    public void sendUpdate(LinkUpdate linkUpdate) {
        for (long id : linkUpdate.tgChatIds()) {
            updateMonitoringBot.sendMessage(id, linkUpdate.description().concat(":\n")
                .concat(linkUpdate.url().toString()));
        }
    }
}
