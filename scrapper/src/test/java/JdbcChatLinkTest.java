import environment.IntegrationEnvironment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class)
@Import(TestConfig.class)
public class JdbcChatLinkTest extends IntegrationEnvironment {
    @Autowired
    private JdbcChatLinkRepository chatLinkRepository;
    @Autowired
    private JdbcLinkRepository linkRepository;
    @Autowired
    private JdbcChatRepository chatRepository;

    @Test
    @Transactional
    public void testAdd() {
        Chat chat = new Chat(1, "Ilya");
        Link link = new Link(1, "url");
        linkRepository.add(link);
        chatRepository.add(chat);
        link = linkRepository.findByUrl(link.url());
        Subscription subscription = new Subscription(chat, link);
        chatLinkRepository.add(subscription);

        var res = chatLinkRepository.findAll();

        assertEquals(res.size(), 1);
        assertTrue(res.contains(subscription));
    }

    @Test
    @Transactional
    public void testRemove() {
        Chat chat = new Chat(1, "Ilya");
        Link link = new Link(1, "url");
        linkRepository.add(link);
        chatRepository.add(chat);
        link = linkRepository.findByUrl(link.url());
        Subscription subscription = new Subscription(chat, link);
        chatLinkRepository.add(subscription);
        chatLinkRepository.remove(subscription);

        var res = chatLinkRepository.findAll();

        assertEquals(res.size(), 0);
    }

    @Test
    @Transactional
    public void testFindAll() {
        Chat chat1 = new Chat(1, "Ilya");
        Link link1 = new Link(1, "url1");
        Chat chat2 = new Chat(2, "Fedya");
        Link link2 = new Link(2, "url2");
        linkRepository.add(link1);
        linkRepository.add(link2);
        chatRepository.add(chat1);
        chatRepository.add(chat2);
        link1 = linkRepository.findByUrl(link1.url());
        link2 = linkRepository.findByUrl(link2.url());
        Subscription subscription1 = new Subscription(chat1, link1);
        Subscription subscription2 = new Subscription(chat2, link2);
        chatLinkRepository.add(subscription1);
        chatLinkRepository.add(subscription2);

        var res = chatLinkRepository.findAll();

        assertEquals(res.size(), 2);
        assertTrue(res.contains(subscription1));
        assertTrue(res.contains(subscription2));
    }

    @Test
    @Transactional
    public void testFindAllByChatId() {
        Chat chat1 = new Chat(1, "Ilya");
        Link link1 = new Link(1, "url1");
        Chat chat2 = new Chat(2, "Fedya");
        Link link2 = new Link(2, "url2");
        linkRepository.add(link1);
        linkRepository.add(link2);
        chatRepository.add(chat1);
        chatRepository.add(chat2);
        link1 = linkRepository.findByUrl(link1.url());
        link2 = linkRepository.findByUrl(link2.url());
        Subscription subscription1 = new Subscription(chat1, link1);
        Subscription subscription2 = new Subscription(chat2, link2);
        chatLinkRepository.add(subscription1);
        chatLinkRepository.add(subscription2);

        var res = chatLinkRepository.findAllByChatId(chat2.id());
        assertEquals(res.size(), 1);
        assertTrue(res.contains(subscription2));
    }

    @Test
    @Transactional
    public void testFindAllByLinkId() {
        Chat chat1 = new Chat(1, "Ilya");
        Link link1 = new Link(1, "url1");
        Chat chat2 = new Chat(2, "Fedya");
        Link link2 = new Link(2, "url2");
        linkRepository.add(link1);
        linkRepository.add(link2);
        chatRepository.add(chat1);
        chatRepository.add(chat2);
        link1 = linkRepository.findByUrl(link1.url());
        link2 = linkRepository.findByUrl(link2.url());
        Subscription subscription1 = new Subscription(chat1, link1);
        Subscription subscription2 = new Subscription(chat2, link2);
        chatLinkRepository.add(subscription1);
        chatLinkRepository.add(subscription2);

        var res = chatLinkRepository.findAllByLinkId(link2.id());
        assertEquals(res.size(), 1);
        assertTrue(res.contains(subscription2));
    }
}
