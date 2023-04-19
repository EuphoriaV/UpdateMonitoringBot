package jooq;

import configuration.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class)
@Import(TestConfig.class)
public class JooqChatTest {
    @Autowired
    private JooqChatRepository chatRepository;

    @Test
    @Transactional
    public void testAdd() {
        Chat chat = new Chat(1, "Ilya");
        chatRepository.add(chat);

        var res = chatRepository.findAll();

        assertEquals(res.get(0), chat);
    }

    @Test
    @Transactional
    public void testRemove() {
        Chat chat = new Chat(1, "Ilya");
        chatRepository.add(chat);
        chatRepository.remove(chat);

        var res = chatRepository.findAll();

        assertEquals(res.size(), 0);
    }

    @Test
    @Transactional
    public void testFindAll() {
        Chat chat1 = new Chat(1, "Ilya");
        chatRepository.add(chat1);
        Chat chat2 = new Chat(2, "Fedya");
        chatRepository.add(chat2);

        var res = chatRepository.findAll();

        assertEquals(res.size(), 2);
        assertTrue(res.contains(chat1));
        assertTrue(res.contains(chat2));
    }

    @Test
    @Transactional
    public void testFindById() {
        Chat chat = new Chat(1, "Ilya");
        chatRepository.add(chat);

        var res = chatRepository.findById(chat.id());

        assertEquals(res, chat);
    }
}
