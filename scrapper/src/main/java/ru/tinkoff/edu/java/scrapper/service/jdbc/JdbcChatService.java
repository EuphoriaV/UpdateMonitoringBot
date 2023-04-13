package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.exceptions.ChatDoesntExistException;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@Service
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

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
