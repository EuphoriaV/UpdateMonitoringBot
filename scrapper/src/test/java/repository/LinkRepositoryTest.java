package repository;

import configuration.TestConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jooq.JooqLinkRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = {ScrapperApplication.class, TestConfig.class})
@ActiveProfiles("test")
public class LinkRepositoryTest {
    @Autowired
    private JooqLinkRepository jooqLinkRepository;
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    public List<LinkRepository> repos() {
        return List.of(jdbcLinkRepository, jooqLinkRepository);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testAdd(int index) {
        LinkRepository linkRepository = repos().get(index);
        Link link = new Link(1, "url");
        linkRepository.add(link);

        var res = linkRepository.findByUrl(link.url());

        assertEquals(res.url(), res.url());
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testRemove(int index) {
        LinkRepository linkRepository = repos().get(index);
        Link link = new Link(1, "url");
        linkRepository.add(link);
        linkRepository.remove(link);

        var res = linkRepository.findAll();

        assertEquals(res.size(), 0);
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindAll(int index) {
        LinkRepository linkRepository = repos().get(index);
        Link link1 = new Link(1, "url1");
        Link link2 = new Link(2, "url2");
        linkRepository.add(link1);
        linkRepository.add(link2);

        var res = linkRepository.findAll().stream().map(Link::url).toList();

        assertEquals(res.size(), 2);
        assertTrue(res.contains(link1.url()));
        assertTrue(res.contains(link2.url()));
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindByUrl(int index) {
        LinkRepository linkRepository = repos().get(index);
        Link link = new Link(1, "url1");
        linkRepository.add(link);

        var res = linkRepository.findByUrl(link.url());

        assertEquals(res.url(), link.url());
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testFindUnchecked(int index) {
        LinkRepository linkRepository = repos().get(index);
        Link link1 = new Link(1, "url1");
        Link link2 = new Link(2, "url2", OffsetDateTime.
                of(2020, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC));
        linkRepository.add(link1);
        linkRepository.add(link2);

        var res = linkRepository.findUnchecked();

        assertEquals(res.size(), 1);
        assertEquals(res.get(0).url(), link2.url());
    }

    @Transactional
    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    public void testUpdateTime(int index) {
        LinkRepository linkRepository = repos().get(index);
        Link link = new Link(1, "url1", OffsetDateTime.
                of(2020, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC));

        linkRepository.updateTime(link);
        var res = linkRepository.findUnchecked();

        assertEquals(res.size(), 0);
    }
}
