package ru.tinkoff.edu.java.scrapper.service;

public interface ChatService {
    void registerChat(long id, String username);

    void unregisterChat(long id);
}
