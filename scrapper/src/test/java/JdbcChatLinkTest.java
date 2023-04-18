import environment.IntegrationEnvironment;
import org.junit.Test;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class JdbcChatLinkTest extends IntegrationEnvironment {
    private final JdbcChatLinkRepository chatLinkRepository = new JdbcChatLinkRepository(jdbcTemplate);
    private final JdbcLinkRepository linkRepository = new JdbcLinkRepository(jdbcTemplate, 300);
    private final JdbcChatRepository chatRepository = new JdbcChatRepository(jdbcTemplate);

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
        Set<Chat> chats = Set.of(new Chat(39, "Eor"), new Chat(89, "Soeone"));
        Set<Link> links = Set.of(new Link(379, "what"), new Link(829, "whaaaaat"));
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

    @Test
    public void testFindAllByChatId() {
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
        var res = chatLinkRepository.findAllByChatId(37);

        var exp = set.stream().filter(subscription -> subscription.chat().id() == 37).toList();
        assertEquals(exp.size(), res.size());
        for (Subscription subscription : exp) {
            assertTrue(res.contains(subscription));
        }
    }

    @Test
    public void testFindAllByLinkId() {
        Set<Chat> chats = Set.of(new Chat(372, "E2or"), new Chat(883, "So2meone"));
        Set<Link> links = Set.of(new Link(3723, "wh1at"), new Link(8284, "whaaaa2at"));
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
        var res = chatLinkRepository.findAllByLinkId(883);

        var exp = set.stream().filter(subscription -> subscription.link().id() == 883).toList();
        assertEquals(exp.size(), res.size());
        for (Subscription subscription : exp) {
            assertTrue(res.contains(subscription));
        }
    }
}
