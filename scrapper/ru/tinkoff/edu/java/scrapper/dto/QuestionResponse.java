package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;

public record QuestionResponse(long id, String title, OffsetDateTime updatedAt) {
}
