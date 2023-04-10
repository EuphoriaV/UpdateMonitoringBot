import environment.IntegrationEnvironment;
import org.junit.Test;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.LinkRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JdbcChatLinkTest extends IntegrationEnvironment {
    private final ChatLinkRepository chatLinkRepository = new ChatLinkRepository(jdbcTemplate);
    private final LinkRepository linkRepository = new LinkRepository(jdbcTemplate);
    private final ChatRepository chatRepository = new ChatRepository(jdbcTemplate);

    @Test
    public void testAdd() {
        Chat chat = new Chat(35, "Sanya");
        Link link = new Link(0, "urlurl");
        linkRepository.add(link);
        chatRepository.add(chat);
        link = linkRepository.findByUrl(link.url());
        Subscription subscription = new Subscription(chat, link);
        chatLinkRepository.add(subscription);

        var res = chatLinkRepository.findAll();

        assertTrue(res.contains(subscription));
    }

    @Test
    public void testRemove() {
        Chat chat = new Chat(36, "Vanya");
        Link link = new Link(0, "urlurlurl");
        linkRepository.add(link);
        chatRepository.add(chat);
        link = linkRepository.findByUrl(link.url());
        Subscription subscription = new Subscription(chat, link);
        chatLinkRepository.add(subscription);
        chatLinkRepository.remove(subscription);

        var res = chatLinkRepository.findAll();

        assertFalse(res.contains(subscription));
    }

    @Test
    public void testFindAll() {
        Set<Chat> chats = Set.of(new Chat(37, "Egor"), new Chat(88, "Someone"));
        Set<Link> links = Set.of(new Link(372, "whaat"), new Link(828, "whaaaaaat"));
        for (Link link : links) {
            linkRepository.add(link);
        }
        for (Chat chat : chats) {
            chatRepository.add(chat);
        }
        Set<Subscription> set = new HashSet<>();
        for (Link link : links) {
            link = linkRepository.findByUrl(link.url());
            for (Chat chat : chats) {
                var sub = new Subscription(chat, link);
                set.add(sub);
                chatLinkRepository.add(sub);
            }
        }
        var res = chatLinkRepository.findAll();

        for (Subscription subscription : set) {
            assertTrue(res.contains(subscription));
        }
    }
}
