package ru.tinkoff.edu.java.scrapper.database.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatLinkRepository;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class JdbcChatLinkRepository implements ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Subscription> rowMapper;

    public JdbcChatLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = ((rs, rowNum) -> new Subscription(
            new Chat(rs.getLong("chat_id"), rs.getString("username")),
            new Link(rs.getLong("link_id"), rs.getString("url"),
                OffsetDateTime.ofInstant(rs.getTimestamp("checked_at").toInstant(), ZoneId.of("UTC"))
            )
        ));
    }

    public List<Subscription> findAll() {
        String sql = "select * from chat_link join chats using(chat_id) join links using(link_id)";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Subscription> findAllByChatId(long chatId) {
        String sql = "select * from chat_link join chats using(chat_id) join links using(link_id) where chat_id = ?";
        return jdbcTemplate.query(sql, rowMapper, chatId);
    }

    public List<Subscription> findAllByLinkId(long linkId) {
        String sql = "select * from chat_link join chats using(chat_id) join links using(link_id) where link_id = ?";
        return jdbcTemplate.query(sql, rowMapper, linkId);
    }

    public void add(Subscription subscription) {
        String sql = "insert into chat_link (chat_id, link_id) values (?,?)";
        jdbcTemplate.update(sql, subscription.chat().id(), subscription.link().id());
    }

    public void remove(Subscription subscription) {
        String sql = "delete from chat_link where chat_id = ? and link_id = ?";
        jdbcTemplate.update(sql, subscription.chat().id(), subscription.link().id());
    }
}
