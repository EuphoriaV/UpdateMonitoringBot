package jdbc;

import configuration.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.ScrapperApplication;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.JdbcLinkRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = ScrapperApplication.class)
@Import(TestConfig.class)
public class JdbcLinkTest {
    @Autowired
    private JdbcLinkRepository linkRepository;

    @Test
    @Transactional
    public void testAdd() {
        Link link = new Link(1, "url");
        linkRepository.add(link);

        var res = linkRepository.findByUrl(link.url());

        assertEquals(res.url(), res.url());
    }

    @Test
    @Transactional
    public void testRemove() {
        Link link = new Link(1, "url");
        linkRepository.add(link);
        linkRepository.remove(link);

        var res = linkRepository.findAll();

        assertEquals(res.size(), 0);
    }

    @Test
    @Transactional
    public void testFindAll() {
        Link link1 = new Link(1, "url1");
        Link link2 = new Link(2, "url2");
        linkRepository.add(link1);
        linkRepository.add(link2);

        var res = linkRepository.findAll().stream().map(Link::url).toList();

        assertEquals(res.size(), 2);
        assertTrue(res.contains(link1.url()));
        assertTrue(res.contains(link2.url()));
    }

    @Test
    @Transactional
    public void testFindByUrl() {
        Link link = new Link(1, "url1");
        linkRepository.add(link);

        var res = linkRepository.findByUrl(link.url());

        assertEquals(res.url(), link.url());
    }

    @Test
    @Transactional
    public void testFindUnchecked() {
        Link link1 = new Link(1, "url1");
        Link link2 = new Link(2, "url2", OffsetDateTime.
                of(2020, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC));
        linkRepository.add(link1);
        linkRepository.add(link2);

        var res = linkRepository.findUnchecked();

        assertEquals(res.size(), 1);
        assertEquals(res.get(0).url(), link2.url());
    }

    @Test
    @Transactional
    public void testUpdateTime() {
        Link link = new Link(1, "url1", OffsetDateTime.
                of(2020, 1, 1, 1, 1, 1, 1, ZoneOffset.UTC));

        linkRepository.updateTime(link);
        var res = linkRepository.findUnchecked();

        assertEquals(res.size(), 0);
    }
}
