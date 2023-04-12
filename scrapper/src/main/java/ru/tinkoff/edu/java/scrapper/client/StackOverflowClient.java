package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.link_parser.StackOverflowQuestion;
import ru.tinkoff.edu.java.scrapper.dto.QuestionResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class StackOverflowClient {
    private final WebClient webClient;

    private final String BASE_URL = "https://api.stackexchange.com/2.3";

    public StackOverflowClient() {
        webClient = WebClient.create(BASE_URL);
    }

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public QuestionResponse fetchQuestion(StackOverflowQuestion question) {
        try {
            JSONObject obj = new JSONObject(requestQuestion(question.id())).
                    getJSONArray("items").getJSONObject(0);
            long id = obj.getLong("question_id");
            String title = obj.getString("title");
            OffsetDateTime updatedAt = OffsetDateTime.of(LocalDateTime.ofEpochSecond(obj.
                    getLong("last_activity_date"), 0, ZoneOffset.UTC), ZoneOffset.UTC);
            long closed = obj.optLong("closed_date", -1);
            OffsetDateTime closedAt = closed == -1 ? null : OffsetDateTime.of(LocalDateTime.ofEpochSecond(
                    closed, 0, ZoneOffset.UTC), ZoneOffset.UTC);
            return new QuestionResponse(id, title, updatedAt, closedAt);
        } catch (JSONException e) {
            return null;
        }
    }

    private String requestQuestion(long id) {
        return webClient.get().uri("/questions/{id}?site=stackoverflow", id).
                retrieve().bodyToMono(String.class).block();
    }
}
