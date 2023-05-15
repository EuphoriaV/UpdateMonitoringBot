package ru.tinkoff.edu.java.scrapper.database.repository.jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.LinkRepository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.LINKS;

@Repository
public class JooqLinkRepository implements LinkRepository {
    private final DSLContext dslContext;
    private final long updateInterval;

    public JooqLinkRepository(DSLContext dslContext, long updateInterval) {
        this.dslContext = dslContext;
        this.updateInterval = updateInterval;
    }

    private Link convert(Record record) {
        return new Link(record.get(LINKS.LINK_ID), record.get(LINKS.URL),
            record.get(LINKS.CHECKED_AT).atZone(ZoneOffset.systemDefault()).toOffsetDateTime()
        );
    }

    public List<Link> findAll() {
        return dslContext.select(LINKS.fields()).from(LINKS).fetch().map(this::convert);
    }

    public Link findByUrl(String url) {
        var res = dslContext.select(LINKS.fields()).from(LINKS)
            .where(LINKS.URL.eq(url)).limit(1).fetch().map(this::convert);
        return res.size() == 0 ? null : res.get(0);
    }

    public void add(Link link) {
        dslContext.insertInto(LINKS).set(LINKS.URL, link.url()).set(
            LINKS.CHECKED_AT,
            LocalDateTime.ofInstant(link.checkedAt().toInstant(), ZoneOffset.systemDefault())
        ).execute();
    }

    public List<Link> findUnchecked() {
        return findAll().stream().filter(link ->
            OffsetDateTime.now().toEpochSecond() - link.checkedAt().toEpochSecond() > updateInterval).toList();
    }

    public void remove(Link link) {
        dslContext.deleteFrom(LINKS).where(LINKS.URL.eq(link.url())).execute();
    }

    public void updateTime(Link link) {
        dslContext.update(LINKS).set(LINKS.CHECKED_AT, LocalDateTime.now()).where(LINKS.URL.eq(link.url())).execute();
    }
}
