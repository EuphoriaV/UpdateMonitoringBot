package ru.tinkoff.edu.java.scrapper.service;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;
import ru.tinkoff.edu.java.scrapper.service.rabbitmq.ScrapperQueueProducer;

@Service
public class MessageService {
    private final boolean useQueue;
    private final BotClient botClient;
    private final ScrapperQueueProducer scrapperQueueProducer;

    public MessageService(ApplicationConfig config, BotClient botClient, ScrapperQueueProducer scrapperQueueProducer) {
        this.useQueue = config.useQueue();
        this.botClient = botClient;
        this.scrapperQueueProducer = scrapperQueueProducer;
    }

    public void sendMessage(LinkUpdate linkUpdate) {
        if (useQueue) {
            scrapperQueueProducer.send(linkUpdate);
        } else {
            botClient.update(linkUpdate);
        }
    }
}
