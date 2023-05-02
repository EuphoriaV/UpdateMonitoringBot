package repository;

import configuration.TestConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqLinkRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
public class ChatLinkRepositoryTest {
    @Autowired
    private JooqChatLinkRepository jooqChatLinkRepository;
    @Autowired
    private JooqLinkRepository jooqLinkRepository;
    @Autowired
    private JooqChatRepository jooqChatRepository;
    @Autowired
    private JdbcChatLinkRepository jdbcChatLinkRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;
    @Autowired
    private JdbcChatRepository jdbcChatRepository;

    public List<LinkRepository> linkRepos() {
        return List.of(jdbcLinkRepository, jooqLinkRepository);
    }

    public List<ChatRepository> chatRepos() {
        return List.of(jdbcChatRepository, jooqChatRepository);
    }

    public List<ChatLinkRepository> chatLinkRepos() {
        return List.of(jdbcChatLinkRepository, jooqChatLinkRepository);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testAdd(int index) {
        LinkRepository linkRepository = linkRepos().get(index);
        ChatRepository chatRepository = chatRepos().get(index);
        ChatLinkRepository chatLinkRepository = chatLinkRepos().get(index);
        Chat chat = new Chat(3, "Vasya");
        Link link = new Link(3, "link");
        linkRepository.add(link);
        chatRepository.add(chat);
        link = linkRepository.findByUrl(link.url());
        Subscription subscription = new Subscription(chat, link);
        chatLinkRepository.add(subscription);

        var res = chatLinkRepository.findAll();

        assertEquals(res.size(), 1);
        assertTrue(res.contains(subscription));
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testRemove(int index) {
        LinkRepository linkRepository = linkRepos().get(index);
        ChatRepository chatRepository = chatRepos().get(index);
        ChatLinkRepository chatLinkRepository = chatLinkRepos().get(index);
        Chat chat = new Chat(3, "Vasya");
        Link link = new Link(3, "link");
        linkRepository.add(link);
        chatRepository.add(chat);
        link = linkRepository.findByUrl(link.url());
        Subscription subscription = new Subscription(chat, link);
        chatLinkRepository.add(subscription);
        chatLinkRepository.remove(subscription);

        var res = chatLinkRepository.findAll();

        assertEquals(res.size(), 0);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindAll(int index) {
        LinkRepository linkRepository = linkRepos().get(index);
        ChatRepository chatRepository = chatRepos().get(index);
        ChatLinkRepository chatLinkRepository = chatLinkRepos().get(index);
        Chat chat1 = new Chat(3, "Vasya");
        Link link1 = new Link(3, "link1");
        Chat chat2 = new Chat(4, "Petya");
        Link link2 = new Link(4, "link2");
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

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindAllByChatId(int index) {
        LinkRepository linkRepository = linkRepos().get(index);
        ChatRepository chatRepository = chatRepos().get(index);
        ChatLinkRepository chatLinkRepository = chatLinkRepos().get(index);
        Chat chat1 = new Chat(3, "Vasya");
        Link link1 = new Link(3, "link1");
        Chat chat2 = new Chat(4, "Petya");
        Link link2 = new Link(4, "link2");
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

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindAllByLinkId(int index) {
        LinkRepository linkRepository = linkRepos().get(index);
        ChatRepository chatRepository = chatRepos().get(index);
        ChatLinkRepository chatLinkRepository = chatLinkRepos().get(index);
        Chat chat1 = new Chat(3, "Vasya");
        Link link1 = new Link(3, "link1");
        Chat chat2 = new Chat(4, "Petya");
        Link link2 = new Link(4, "link2");
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
