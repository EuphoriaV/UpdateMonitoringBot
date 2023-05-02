package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.exceptions.ChatDoesntExistException;

public abstract class AbstractChatService implements ChatService {
    protected ChatRepository chatRepository;

    public void registerChat(long id, String username) {
        var chat = chatRepository.findById(id);
        if (chat == null) {
            chatRepository.add(new Chat(id, username));
        }
    }

    public void unregisterChat(long id) {
        var chat = chatRepository.findById(id);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + id + " doesn't exist");
        }
        chatRepository.remove(chat);
    }
}
