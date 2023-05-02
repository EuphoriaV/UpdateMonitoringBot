package ru.tinkoff.edu.java.scrapper.database.repository;

import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;

import java.util.List;

public interface ChatLinkRepository {
    List<Subscription> findAll();

    List<Subscription> findAllByChatId(long chatId);

    List<Subscription> findAllByLinkId(long linkId);

    void add(Subscription subscription);

    void remove(Subscription subscription);
}
