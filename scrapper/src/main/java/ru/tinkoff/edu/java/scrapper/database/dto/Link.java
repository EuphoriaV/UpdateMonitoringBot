package ru.tinkoff.edu.java.scrapper.database.dto;

import java.time.OffsetDateTime;
import java.util.Objects;

public record Link(long id, String url, OffsetDateTime checkedAt) {
    public Link(long id, String url) {
        this(id, url, OffsetDateTime.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Link link = (Link) o;
        return id == link.id && Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url);
    }
}
