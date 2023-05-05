package ru.tinkoff.edu.java.scrapper.client;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.dto.LinkUpdate;

public class BotClient {
    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8081";

    public BotClient() {
        webClient = WebClient.create(BASE_URL);
    }

    public BotClient(String baseUrl) {
        this.webClient = WebClient.create(baseUrl);
    }

    public void update(LinkUpdate linkUpdate) {
        webClient.post().uri("/updates").bodyValue(linkUpdate).retrieve().toBodilessEntity().block();
    }
}
