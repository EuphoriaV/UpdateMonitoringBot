package ru.tinkoff.edu.java.scrapper.database.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;

import java.util.List;

@Repository
public class ChatLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Subscription> rowMapper;

    public ChatLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = ((rs, rowNum) -> new Subscription(
                new Chat(rs.getLong("chat_id"), rs.getString("username")),
                new Link(rs.getLong("link_id"), rs.getString("url"))
        ));
    }

    public List<Subscription> findAll() {
        String sql = "select * from chat_link join chats using(chat_id) join links using(link_id)";
        return jdbcTemplate.query(sql, rowMapper);
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
