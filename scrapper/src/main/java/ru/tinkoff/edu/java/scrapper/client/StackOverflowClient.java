package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;
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

    public QuestionResponse fetchQuestion(long id) {
        try {
            JSONObject obj = new JSONObject(requestQuestion(id)).getJSONArray("items").getJSONObject(0);
            return new QuestionResponse(obj.getLong("question_id"), obj.getString("title"),
                    OffsetDateTime.of(LocalDateTime.ofEpochSecond(obj.getLong("last_edit_date"),
                            0, ZoneOffset.UTC), ZoneOffset.UTC));
        } catch (JSONException e) {
            return null;
        }
    }

    private String requestQuestion(long id) {
        return webClient.get().uri("/questions/{id}?site=stackoverflow", id).
                retrieve().bodyToMono(String.class).share().block();
    }
}
