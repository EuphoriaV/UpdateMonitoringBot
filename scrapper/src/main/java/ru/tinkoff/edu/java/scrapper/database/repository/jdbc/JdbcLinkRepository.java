package ru.tinkoff.edu.java.scrapper.database.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.LinkRepository;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class JdbcLinkRepository implements LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> rowMapper;

    private final long updateInterval;

    public JdbcLinkRepository(JdbcTemplate jdbcTemplate, long updateInterval) {
        this.jdbcTemplate = jdbcTemplate;
        this.updateInterval = updateInterval;
        rowMapper = ((rs, rowNum) -> new Link(
                rs.getLong("link_id"),
                rs.getString("url"),
                OffsetDateTime.ofInstant(rs.getTimestamp("checked_at").toInstant(), ZoneId.of("UTC"))
        ));
    }

    public List<Link> findAll() {
        String sql = "select * from links";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Link findByUrl(String url) {
        String sql = "select * from links where url = ? limit 1";
        var res = jdbcTemplate.query(sql, rowMapper, url);
        return res.size() > 0 ? res.get(0) : null;
    }

    public void add(Link link) {
        String sql = "insert into links (url, checked_at) values (?, ?)";
        jdbcTemplate.update(sql, link.url(), link.checkedAt());
    }

    public List<Link> findUnchecked() {
        String sql = "select * from links where checked_at < now() - interval '" + updateInterval + " seconds'";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void remove(Link link) {
        String sql = "delete from links where url = ?";
        jdbcTemplate.update(sql, link.url());
    }

    public void updateTime(Link link) {
        String sql = "update links set checked_at = now() where url = ?";
        jdbcTemplate.update(sql, link.url());
    }
}
