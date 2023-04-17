package ru.tinkoff.edu.java.scrapper.database.repository;

import ru.tinkoff.edu.java.scrapper.database.dto.Chat;

import java.util.List;

public interface ChatRepository {
    List<Chat> findAll();

    Chat findById(long id);

    void add(Chat chat);

    void remove(Chat chat);
}
