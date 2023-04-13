package ru.tinkoff.edu.java.scrapper.database.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Chat;

import java.util.List;

@Repository
public class JdbcChatRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Chat> rowMapper;

    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = ((rs, rowNum) -> new Chat(
                rs.getLong("chat_id"),
                rs.getString("username")
        ));
    }

    public List<Chat> findAll() {
        String sql = "select * from chats";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Chat findById(long id) {
        String sql = "select * from chats where chat_id = ?";
        var res = jdbcTemplate.query(sql, rowMapper, id);
        return res.size() > 0 ? res.get(0) : null;
    }

    public void add(Chat chat) {
        String sql = "insert into chats (chat_id, username) values (?, ?)";
        jdbcTemplate.update(sql, chat.id(), chat.username());
    }

    public void remove(Chat chat) {
        String sql = "delete from chats where chat_id = ?";
        jdbcTemplate.update(sql, chat.id());
    }
}
