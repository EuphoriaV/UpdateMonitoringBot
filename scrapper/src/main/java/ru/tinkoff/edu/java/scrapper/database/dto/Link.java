package ru.tinkoff.edu.java.scrapper.database.dto;

import java.time.OffsetDateTime;

public record Link(long id, String url, OffsetDateTime checkedAt) {
    public Link(long id, String url) {
        this(id, url, OffsetDateTime.now());
    }
}
