package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;

public record RepositoryResponse(String fullName, OffsetDateTime pushedAt) {
}
