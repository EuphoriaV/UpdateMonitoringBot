package ru.tinkoff.edu.java.scrapper.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record QuestionResponse(long id, String title, OffsetDateTime updatedAt,
                               OffsetDateTime closedAt, List<OffsetDateTime> answersTime) {
}
