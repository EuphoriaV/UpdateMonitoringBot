package service;

import configuration.TestConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class)
@Import(TestConfig.class)
public class ChatServiceTest {
    @Autowired
    private JdbcChatService jdbcChatService;
    @Autowired
    private JooqChatService jooqChatService;
    @Autowired
    private JpaChatService jpaChatService;
    @Autowired
    private JpaChatRepository chatRepository;


    public List<ChatService> services() {
        return List.of(jdbcChatService, jooqChatService, jpaChatService);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testRegisterChat(int index) {
        ChatService chatService = services().get(index);
        Chat chat = new Chat(1, "Ilya");

        chatService.registerChat(chat.id(), chat.username());

        assertTrue(chatRepository.existsById(chat.id()));
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testUnregisterChat(int index) {
        ChatService chatService = services().get(index);
        Chat chat = new Chat(1, "Ilya");
        chatService.registerChat(chat.id(), chat.username());
        assertTrue(chatRepository.existsById(chat.id()));

        chatService.unregisterChat(chat.id());

        assertFalse(chatRepository.existsById(chat.id()));
    }
}
