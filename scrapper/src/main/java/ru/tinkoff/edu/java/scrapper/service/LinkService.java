package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;

public interface LinkService {
    ListLinksResponse findAll(long tgChatId);

    LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest);

    LinkResponse removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest);
}
