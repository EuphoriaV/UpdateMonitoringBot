package repository;

import configuration.TestConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class)
@Import(TestConfig.class)
public class ChatRepositoryTest {
    @Autowired
    private JdbcChatRepository jdbcChatRepository;
    @Autowired
    private JooqChatRepository jooqChatRepository;

    public List<ChatRepository> repos() {
        return List.of(jdbcChatRepository, jooqChatRepository);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testAdd(int index) {
        ChatRepository chatRepository = repos().get(index);
        Chat chat = new Chat(1, "Ilya");
        chatRepository.add(chat);

        var res = chatRepository.findAll();

        assertEquals(res.get(0), chat);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testRemove(int index) {
        ChatRepository chatRepository = repos().get(index);
        Chat chat = new Chat(1, "Ilya");
        chatRepository.add(chat);
        chatRepository.remove(chat);

        var res = chatRepository.findAll();

        assertEquals(res.size(), 0);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindAll(int index) {
        ChatRepository chatRepository = repos().get(index);
        Chat chat1 = new Chat(1, "Ilya");
        chatRepository.add(chat1);
        Chat chat2 = new Chat(2, "Fedya");
        chatRepository.add(chat2);

        var res = chatRepository.findAll();

        assertEquals(res.size(), 2);
        assertTrue(res.contains(chat1));
        assertTrue(res.contains(chat2));
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindById(int index) {
        ChatRepository chatRepository = repos().get(index);
        Chat chat = new Chat(1, "Ilya");
        chatRepository.add(chat);

        var res = chatRepository.findById(chat.id());

        assertEquals(res, chat);
    }
}
