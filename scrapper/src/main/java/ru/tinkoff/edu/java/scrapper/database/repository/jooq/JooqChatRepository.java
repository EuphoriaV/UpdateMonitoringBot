package ru.tinkoff.edu.java.scrapper.database.repository.jooq;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.domain.jooq.Tables.CHATS;

@Repository
public class JooqChatRepository implements ChatRepository {
    private final DSLContext dslContext;

    public JooqChatRepository(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

    public List<Chat> findAll() {
        return dslContext.select().from(CHATS).fetchInto(Chat.class);
    }

    public Chat findById(long id) {
        var res = dslContext.select(CHATS.fields()).from(CHATS).
                where(CHATS.CHAT_ID.eq((int) id)).limit(1).fetchInto(Chat.class);
        return res.size() == 0 ? null : res.get(0);
    }

    public void add(Chat chat) {
        dslContext.insertInto(CHATS).set(CHATS.CHAT_ID, (int) chat.id()).set(CHATS.USERNAME, chat.username()).execute();
    }

    public void remove(Chat chat) {
        dslContext.deleteFrom(CHATS).where(CHATS.CHAT_ID.eq((int) chat.id())).execute();
    }
}
