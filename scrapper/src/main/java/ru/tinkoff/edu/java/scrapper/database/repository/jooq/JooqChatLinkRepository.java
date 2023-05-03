package ru.tinkoff.edu.java.scrapper.database.repository.jooq;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatLinkRepository;

import java.time.ZoneOffset;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.*;

@Repository
public class JooqChatLinkRepository implements ChatLinkRepository {
    private final DSLContext dslContext;

    public JooqChatLinkRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    private Subscription convert(Record record) {
        return new Subscription(new Chat(record.get(CHATS.CHAT_ID), record.get(CHATS.USERNAME)),
                new Link(record.get(LINKS.LINK_ID), record.get(LINKS.URL),
                        record.get(LINKS.CHECKED_AT).atOffset(ZoneOffset.UTC)));
    }

    public List<Subscription> findAll() {
        return dslContext.select().from(CHAT_LINK).join(CHATS).using(CHATS.CHAT_ID).
                join(LINKS).using(CHAT_LINK.LINK_ID).fetch().map(this::convert);
    }

    public List<Subscription> findAllByChatId(long chatId) {
        return dslContext.select().from(CHAT_LINK).join(CHATS).using(CHATS.CHAT_ID).
                join(LINKS).using(CHAT_LINK.LINK_ID).where(CHATS.CHAT_ID.eq((int) chatId)).fetch().map(this::convert);
    }

    public List<Subscription> findAllByLinkId(long linkId) {
        return dslContext.select().from(CHAT_LINK).join(CHATS).using(CHATS.CHAT_ID).
                join(LINKS).using(CHAT_LINK.LINK_ID).where(LINKS.LINK_ID.eq((int) linkId)).fetch().map(this::convert);
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
