import environment.IntegrationEnvironment;
import org.junit.Test;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.jdbc.LinkRepository;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class JdbcLinkTest extends IntegrationEnvironment {
    private final LinkRepository linkRepository = new LinkRepository(jdbcTemplate);

    @Test
    public void testAdd() {
        Link link = new Link(123123123, "url5");
        linkRepository.add(link);

        var res = linkRepository.findAll();

        assertTrue(res.stream().map(Link::url).anyMatch(url -> url.equals(link.url())));
    }

    @Test
    public void testRemove() {
        Link link = new Link(1231231232, "url6");
        linkRepository.add(link);
        linkRepository.remove(link);

        var res = linkRepository.findAll();

        assertTrue(res.stream().map(Link::url).noneMatch(url -> url.equals(link.url())));
    }

    @Test
    public void testFindAll() {
        Set<String> set = Set.of("url1", "url2", "url3", "url4");
        for (String link : set) {
            linkRepository.add(new Link(0, link));
        }

        var res = linkRepository.findAll();

        for (String url : set) {
            assertTrue(res.stream().map(Link::url).anyMatch(url1 -> url1.equals(url)));
        }
    }

    @Test
    public void testFindByUrl() {
        Link link = new Link(1200, "url7");
        linkRepository.add(link);

        var res = linkRepository.findByUrl(link.url());

        assertNotNull(res);
        assertEquals(res.url(), link.url());
    }

    @Test
    public void testFindUnchecked() {
        Set<String> set = Set.of("url12", "url22", "url32", "url42");
        for (String link : set) {
            linkRepository.add(new Link(0, link));
        }

        var res = linkRepository.findUnchecked();

        for (String url : set) {
            assertTrue(res.stream().map(Link::url).noneMatch(url1 -> url1.equals(url)));
        }

        Set<String> set1 = Set.of("url123", "url224", "url532", "url423");
        for (String link : set1) {
            linkRepository.add(new Link(0, link, OffsetDateTime.
                    of(2020,1,1,1,1,1,1, ZoneOffset.UTC)));
        }

        var res1 = linkRepository.findUnchecked();

        for (String url : set1) {
            assertTrue(res1.stream().map(Link::url).anyMatch(url1 -> url1.equals(url)));
        }
    }
}
