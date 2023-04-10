package ru.tinkoff.edu.java.scrapper.database.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;

import java.util.List;

@Repository
public class LinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Link> rowMapper;

    public LinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        rowMapper = ((rs, rowNum) -> new Link(
                rs.getLong("link_id"),
                rs.getString("url")
        ));
    }

    public List<Link> findAll() {
        String sql = "select * from links";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Link findByUrl(String url) {
        String sql = "select * from links where url = ?";
        var res = jdbcTemplate.query(sql, rowMapper, url);
        return res.size() > 0 ? res.get(0) : null;
    }

    public void add(Link link) {
        String sql = "insert into links (url) values (?)";
        jdbcTemplate.update(sql, link.url());
    }

    public void remove(Link link) {
        String sql = "delete from links where url = ?";
        jdbcTemplate.update(sql, link.url());
    }
}
