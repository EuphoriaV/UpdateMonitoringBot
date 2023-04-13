package ru.tinkoff.edu.java.scrapper.database.repository.jooq;

import org.jooq.DSLContext;
import org.jooq.Record5;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.*;

@Repository
public class JooqChatLinkRepository {
    private final DSLContext dslContext;

    public JooqChatLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    private Subscription convert(Record5 record5) {
        return new Subscription(new Chat((int) record5.get("chat_id"), (String) record5.get("username")),
                new Link((int) record5.get("link_id"), (String) record5.get("url"),
                        ((LocalDateTime) record5.get("checked_at")).atOffset(ZoneOffset.UTC)));
    }

    public List<Subscription> findAll() {
        var res = dslContext.select(CHATS.CHAT_ID, CHATS.USERNAME,
                        LINKS.LINK_ID, LINKS.URL, LINKS.CHECKED_AT).from(CHAT_LINK).join(CHATS).using(CHATS.CHAT_ID).
                join(LINKS).using(CHAT_LINK.LINK_ID).fetch();
        return res.stream().map(this::convert).toList();
    }

    public List<Subscription> findAllByChatId(long chatId) {
        var res = dslContext.select(CHATS.CHAT_ID, CHATS.USERNAME,
                        LINKS.LINK_ID, LINKS.URL, LINKS.CHECKED_AT).from(CHAT_LINK).join(CHATS).using(CHATS.CHAT_ID).
                join(LINKS).using(CHAT_LINK.LINK_ID).where(CHATS.CHAT_ID.eq((int) chatId)).fetch();
        return res.stream().map(this::convert).toList();
    }

    public List<Subscription> findAllByLinkId(long linkId) {
        var res = dslContext.select(CHATS.CHAT_ID, CHATS.USERNAME,
                        LINKS.LINK_ID, LINKS.URL, LINKS.CHECKED_AT).from(CHAT_LINK).join(CHATS).using(CHATS.CHAT_ID).
                join(LINKS).using(CHAT_LINK.LINK_ID).where(LINKS.LINK_ID.eq((int) linkId)).fetch();
        return res.stream().map(this::convert).toList();
    }

    public void add(Subscription subscription) {
        dslContext.insertInto(CHAT_LINK).set(CHAT_LINK.CHAT_ID, (int) subscription.chat().id()).
                set(CHAT_LINK.LINK_ID, (int) subscription.link().id()).execute();
    }

    public void remove(Subscription subscription) {
        dslContext.deleteFrom(CHAT_LINK).where(CHAT_LINK.CHAT_ID.eq((int) subscription.chat().id())).
                and(CHAT_LINK.LINK_ID.eq((int) subscription.link().id())).execute();
    }
}
