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
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidParametersException;
import ru.tinkoff.edu.java.scrapper.exceptions.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqChatService;
import ru.tinkoff.edu.java.scrapper.service.jooq.JooqLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
public class LinkServiceTest {
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


    public List<LinkService> linkServices() {
        return List.of(jdbcLinkService, jooqLinkService, jpaLinkService);
    }

    @SneakyThrows
    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testFindAll(int index) {
        LinkService linkService = linkServices().get(index);
        ChatService chatService = chatServices().get(index);
        Chat chat = new Chat(1, "12312");
        String link1 = "https://github.com/EuphoriaV/WeChat";
        String link2 = "https://github.com/EuphoriaV/Tinkoff-java-backend";
        chatService.registerChat(chat.id(), chat.username());
        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link1)));
        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link2)));

        var res = linkService.findAll(chat.id()).links().stream().map(LinkResponse::url).map(URI::toString).toList();

        assertEquals(res.size(), 2);
        assertTrue(res.contains(link1));
        assertTrue(res.contains(link2));
    }

    @SneakyThrows
    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testAddLink(int index) {
        LinkService linkService = linkServices().get(index);
        ChatService chatService = chatServices().get(index);
        Chat chat = new Chat(1, "12312");
        String link = "https://github.com/EuphoriaV/WeChat";
        String invalidLink = "https://githu.com/EuphoriaV/WeChat";
        chatService.registerChat(chat.id(), chat.username());

        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link)));

        var res = linkService.findAll(chat.id());
        assertEquals(res.links().get(0).url().toString(), link);
        assertThrows(InvalidParametersException.class, () -> linkService.addLink(chat.id(),
                new AddLinkRequest(new URI(invalidLink))));
    }

    @SneakyThrows
    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    public void testRemoveLink(int index) {
        LinkService linkService = linkServices().get(index);
        ChatService chatService = chatServices().get(index);
        Chat chat = new Chat(1, "12312");
        String link = "https://github.com/EuphoriaV/WeChat";
        String invalidLink = "https://github.com/EuphoriaV/WeChat1";
        chatService.registerChat(chat.id(), chat.username());
        linkService.addLink(chat.id(), new AddLinkRequest(new URI(link)));

        linkService.removeLink(chat.id(), new RemoveLinkRequest(new URI(link)));

        var res = linkService.findAll(chat.id());
        assertEquals(res.size(), 0);
        assertThrows(LinkNotFoundException.class, () -> linkService.removeLink(chat.id(),
                new RemoveLinkRequest(new URI(invalidLink))));
    }
}
