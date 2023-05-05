package ru.tinkoff.edu.java.bot.service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;
import ru.tinkoff.edu.java.bot.service.BotService;

@Service
@RabbitListener(queues = "${bot.queue-name}")
public class ScrapperQueueListener {
    private final BotService botService;

    public ScrapperQueueListener(BotService botService) {
        this.botService = botService;
    }

    @RabbitHandler
    public void listen(LinkUpdate update) {
        botService.sendUpdate(new ru.tinkoff.edu.java.bot.dto.LinkUpdate(
            update.id(), update.url(), update.description(), update.tgChatIds()));
    }
}

