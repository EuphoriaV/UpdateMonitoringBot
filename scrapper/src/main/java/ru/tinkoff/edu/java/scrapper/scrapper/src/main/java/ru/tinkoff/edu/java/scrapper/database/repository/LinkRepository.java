package ru.tinkoff.edu.java.scrapper.database.repository;

import ru.tinkoff.edu.java.scrapper.database.dto.Link;

import java.util.List;

public interface LinkRepository {
    List<Link> findAll();

    Link findByUrl(String url);

    void add(Link link);

    List<Link> findUnchecked();

    void remove(Link link);

    void updateTime(Link link);
}
