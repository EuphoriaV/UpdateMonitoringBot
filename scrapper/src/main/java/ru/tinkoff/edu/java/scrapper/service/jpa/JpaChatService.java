package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.database.entity.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@Service
@Primary
public class JpaChatService implements ChatService {
    private final JpaChatRepository jpaChatRepository;

    public JpaChatService(JpaChatRepository jpaChatRepository) {
        this.jpaChatRepository = jpaChatRepository;
    }

    public void registerChat(long id, String username) {
        Chat chat = new Chat();
        chat.setId(id);
        chat.setUsername(username);
        jpaChatRepository.save(chat);
    }

    public void unregisterChat(long id) {
        Chat chat = new Chat();
        chat.setId(id);
        jpaChatRepository.delete(chat);
    }
}
