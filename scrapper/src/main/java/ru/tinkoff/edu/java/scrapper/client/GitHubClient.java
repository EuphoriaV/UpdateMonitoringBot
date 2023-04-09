package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.RepositoryResponse;

import java.time.OffsetDateTime;

public class GitHubClient {
    private final WebClient webClient;
    private final String BASE_URL = "https://api.github.com";

    public GitHubClient() {
        webClient = WebClient.create(BASE_URL);
    }

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public RepositoryResponse fetchRepository(String user, String repository) {
        try {
            JSONObject obj = new JSONObject(requestRepository(user, repository));
            return new RepositoryResponse(obj.getString("full_name"),
                    OffsetDateTime.parse(obj.getString("updated_at")));
        } catch (JSONException e) {
            return null;
        }
    }

    private String requestRepository(String user, String repository) {
        return webClient.get().uri("/repos/{user}/{repo}", user, repository).
                retrieve().bodyToMono(String.class).block();
    }
}
