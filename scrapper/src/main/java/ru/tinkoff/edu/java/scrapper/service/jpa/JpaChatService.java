package ru.tinkoff.edu.java.scrapper.service.jpa;

import ru.tinkoff.edu.java.scrapper.database.entity.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.exceptions.ChatDoesntExistException;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

public class JpaChatService implements ChatService {
    private final JpaChatRepository jpaChatRepository;

    public JpaChatService(JpaChatRepository jpaChatRepository) {
        this.jpaChatRepository = jpaChatRepository;
    }

    public void registerChat(long id, String username) {
        if (!jpaChatRepository.existsById(id)) {
            Chat chat = new Chat();
            chat.setId(id);
            chat.setUsername(username);
            jpaChatRepository.save(chat);
        }
    }

    public void unregisterChat(long id) {
        Chat chat = jpaChatRepository.findById(id).orElse(null);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + id + " doesn't exist");
        }
        jpaChatRepository.delete(chat);
    }
}
