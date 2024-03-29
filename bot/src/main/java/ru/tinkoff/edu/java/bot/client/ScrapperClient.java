package ru.tinkoff.edu.java.bot.client;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.dto.ListLinksResponse;
import ru.tinkoff.edu.java.bot.dto.RemoveLinkRequest;

public class ScrapperClient {
    private final WebClient webClient;

    private final String BASE_URL = "http://localhost:8080";

    public ScrapperClient() {
        webClient = WebClient.create(BASE_URL);
    }

    public void createChat(long id, String username) {
        webClient.post().uri("/tg-chat/{id}", id).header("username", username).retrieve().toBodilessEntity().block();
    }

    public void deleteChat(long id) {
        webClient.delete().uri("/tg-chat/{id}", id).retrieve().toBodilessEntity().block();
    }

    public ListLinksResponse getLinks(long tgChatId) {
        return webClient.get().uri("/links").header("tgChatId", String.valueOf(tgChatId))
            .retrieve().bodyToMono(ListLinksResponse.class).block();
    }

    public LinkResponse addLink(long tgChatId, AddLinkRequest request) {
        return webClient.post().uri("/links").header("tgChatId", String.valueOf(tgChatId))
            .bodyValue(request).retrieve().bodyToMono(LinkResponse.class).block();
    }

    public LinkResponse deleteLink(long tgChatId, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE).uri("/links").header(
            "tgChatId",
            String.valueOf(tgChatId)
        ).bodyValue(request).retrieve().bodyToMono(LinkResponse.class).block();
    }
}
