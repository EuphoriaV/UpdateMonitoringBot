package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfiguration {
    @Bean
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.scrapper.dto.LinkUpdate", LinkUpdate.class);
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.scrapper.dto.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
    }

    @Bean
    public Queue scrapperQueue(ApplicationConfig config) {
        return QueueBuilder.durable(config.queueName())
                .withArgument("x-dead-letter-exchange", config.queueName().concat(".dlx"))
                .build();
    }

    @Bean
    public DirectExchange scrapperExchange(ApplicationConfig config) {
        return new DirectExchange(config.exchangeName());
    }

    @Bean
    public Binding bindingScrapper(Queue scrapperQueue, DirectExchange scrapperExchange) {
        return BindingBuilder.bind(scrapperQueue).to(scrapperExchange).withQueueName();
    }

    @Bean
    public FanoutExchange deadLetterExchange(ApplicationConfig config) {
        return new FanoutExchange(config.queueName().concat(".dlx"));
    }

    @Bean
    public Queue deadLetterQueue(ApplicationConfig config) {
        return QueueBuilder.durable(config.queueName().concat(".dlq")).build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
    }
}
