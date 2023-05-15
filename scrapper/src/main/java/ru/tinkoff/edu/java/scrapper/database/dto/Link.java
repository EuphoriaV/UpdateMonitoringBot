package ru.tinkoff.edu.java.scrapper.database.dto;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record Link(long id, String url, OffsetDateTime checkedAt) {
    public Link(long id, String url) {
        this(id, url, OffsetDateTime.now(ZoneOffset.UTC));
    }
}
