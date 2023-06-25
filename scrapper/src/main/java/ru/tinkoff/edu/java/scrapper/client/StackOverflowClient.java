package ru.tinkoff.edu.java.scrapper.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.link_parser.StackOverflowQuestion;
import ru.tinkoff.edu.java.scrapper.dto.QuestionResponse;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class StackOverflowClient {
    private final WebClient webClient;

    private final String BASE_URL = "https://api.stackexchange.com/2.3";

    public StackOverflowClient() {
        webClient = WebClient.create(BASE_URL);
    }

    public QuestionResponse fetchQuestion(StackOverflowQuestion question) {
        try {
            JSONObject obj = new JSONObject(requestQuestion(question.id()))
                .getJSONArray("items").getJSONObject(0);
            long id = obj.getLong("question_id");
            String title = obj.getString("title");
            OffsetDateTime updatedAt = convert(obj.getLong("last_activity_date"));
            long closed = obj.optLong("closed_date", -1);
            OffsetDateTime closedAt = closed == -1 ? null : convert(closed);
            var answersTime = getAnswersTime(question.id());
            return new QuestionResponse(id, title, updatedAt, closedAt, answersTime);
        } catch (JSONException e) {
            return null;
        }
    }

    private List<OffsetDateTime> getAnswersTime(long id) {
        try {
            JSONArray answers = new JSONObject(requestQuestionAnswers(id)).getJSONArray("items");
            List<OffsetDateTime> list = new ArrayList<>();
            for (int i = 0; i < answers.length(); i++) {
                list.add(convert(answers.getJSONObject(i).getLong("creation_date")));
            }
            return list;
        } catch (JSONException e) {
            return null;
        }
    }

    private OffsetDateTime convert(long time) {
        return OffsetDateTime.of(LocalDateTime.ofEpochSecond(time, 0, ZoneOffset.UTC), ZoneOffset.UTC);
    }

    private String requestQuestion(long id) {
        return webClient.get().uri("/questions/{id}?site=stackoverflow", id)
            .retrieve().bodyToMono(String.class).block();
    }

    private String requestQuestionAnswers(long id) {
        return webClient.get().uri("/questions/{id}/answers?site=stackoverflow", id)
            .retrieve().bodyToMono(String.class).block();
    }
}
