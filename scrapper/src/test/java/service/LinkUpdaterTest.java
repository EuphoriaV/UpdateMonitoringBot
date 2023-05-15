package service;

import configuration.TestConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkUpdater;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
public class LinkUpdaterTest {
    @Autowired
    private JdbcChatService jdbcChatService;
    @Autowired
    private JooqChatService jooqChatService;
    @Autowired
    private JpaChatService jpaChatService;

    public List<ChatService> chatServices() {
        return List.of(jdbcChatService, jooqChatService, jpaChatService);
    }

    @Autowired
    private JdbcLinkService jdbcLinkService;
    @Autowired
    private JooqLinkService jooqLinkService;
    @Autowired
    private JpaLinkService jpaLinkService;
    @Autowired
    private JdbcLinkRepository linkRepository;

    public List<LinkService> linkServices() {
        return List.of(jdbcLinkService, jooqLinkService, jpaLinkService);
    }

    @Autowired
    private JdbcLinkUpdater jdbcLinkUpdater;
    @Autowired
    private JooqLinkUpdater jooqLinkUpdater;
    @Autowired
    private JpaLinkUpdater jpaLinkUpdater;

    public List<LinkUpdater> updaters() {
        return List.of(jdbcLinkUpdater, jooqLinkUpdater, jpaLinkUpdater);
    }

    @SneakyThrows
    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testGetUpdates(int index) {
        LinkUpdater linkUpdater = updaters().get(index);
        ChatService chatService = chatServices().get(index);
        LinkService linkService = linkServices().get(index);
        Chat chat = new Chat(1, "Ilya");
        chatService.registerChat(chat.id(), chat.username());
        Link link1 = new Link(1, "https://github.com/EuphoriaV/WeChat");
        Link link2 = new Link(1, "https://github.com/EuphoriaV/WeChat1", OffsetDateTime.now().minusDays(23));
        linkRepository.add(link1);
        linkRepository.add(link2);
        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link1.url())));
        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link2.url())));

        var res = linkUpdater.getUpdates();
        assertEquals(res.size(), 1);
        assertEquals(res.get(0).link().url(), link2.url());
    }

    @SneakyThrows
    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testUpdateAll(int index) {
        LinkUpdater linkUpdater = updaters().get(index);
        ChatService chatService = chatServices().get(index);
        LinkService linkService = linkServices().get(index);
        Chat chat = new Chat(1, "Ilya");
        chatService.registerChat(chat.id(), chat.username());
        Link link1 = new Link(1, "https://github.com/EuphoriaV/WeChat");
        Link link2 = new Link(1, "https://github.com/EuphoriaV/WeChat1", OffsetDateTime.now().minusDays(23));
        linkRepository.add(link1);
        linkRepository.add(link2);
        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link1.url())));
        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link2.url())));

        linkUpdater.updateAll(List.of(link2));

        var res = linkUpdater.getUpdates();
        assertEquals(res.size(), 0);
    }
}
