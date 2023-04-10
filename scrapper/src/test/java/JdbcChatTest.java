import environment.IntegrationEnvironment;
import org.junit.Test;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcChatTest extends IntegrationEnvironment {
    private final ChatRepository chatRepository = new ChatRepository(jdbcTemplate);

    @Test
    public void testAdd() {
        Chat chat = new Chat(123123123, "Ilya");
        chatRepository.add(chat);

        var res = chatRepository.findAll();

        assertTrue(res.contains(chat));
    }

    @Test
    public void testRemove() {
        Chat chat = new Chat(1231, "Fedor");
        chatRepository.add(chat);
        chatRepository.remove(chat);

        var res = chatRepository.findAll();

        assertFalse(res.contains(chat));
    }

    @Test
    public void testFindAll() {
        Set<Chat> set = Set.of(new Chat(123, "Misha"), new Chat(1234, "Masha"),
                new Chat(12345, "Pasha"), new Chat(123456, "Sasha"));
        for (Chat chat : set) {
            chatRepository.add(chat);
        }

        var res = chatRepository.findAll();

        for (Chat chat : set) {
            assertTrue(res.contains(chat));
        }
    }

    @Test
    public void testFindById() {
        Chat chat = new Chat(1200, "Ktoto");
        chatRepository.add(chat);

        var res = chatRepository.findById(chat.id());

        assertNotNull(res);
        assertTrue(res.equals(chat));
    }
}
