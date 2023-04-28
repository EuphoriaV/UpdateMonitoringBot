package ru.tinkoff.edu.java.scrapper.service.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.configuration.ApplicationConfig;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

@Service
public class ScrapperQueueProducer {
    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String queue;

    public ScrapperQueueProducer(RabbitTemplate rabbitTemplate, ApplicationConfig applicationConfig) {
        this.rabbitTemplate = rabbitTemplate;
        exchange = applicationConfig.exchangeName();
        queue = applicationConfig.queueName();
    }

    public void send(LinkUpdate linkUpdate) {
        rabbitTemplate.convertAndSend(exchange, queue, linkUpdate);
    }
}
