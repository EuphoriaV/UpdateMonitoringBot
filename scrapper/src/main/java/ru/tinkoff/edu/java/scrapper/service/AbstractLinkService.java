package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.link_parser.LinkParser;
import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatLinkRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.ChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.LinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.exceptions.ChatDoesntExistException;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidParametersException;
import ru.tinkoff.edu.java.scrapper.exceptions.LinkNotFoundException;

import java.net.URI;
import java.net.URISyntaxException;

public class AbstractLinkService implements LinkService {
    protected ChatLinkRepository chatLinkRepository;
    protected LinkRepository linkRepository;
    protected ChatRepository chatRepository;

    public ListLinksResponse findAll(long tgChatId) {
        var chat = chatRepository.findById(tgChatId);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + tgChatId + " doesn't exist");
        }
        var res = chatLinkRepository.findAllByChatId(tgChatId);
        var list = res.stream().map(subscription -> convert(subscription.link())).toList();
        return new ListLinksResponse(list, list.size());
    }

    public LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest) {
        var chat = chatRepository.findById(tgChatId);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + tgChatId + " doesn't exist");
        }
        String url = addLinkRequest.link().toString();
        var parseRes = LinkParser.parseLink(url);
        if (parseRes == null) {
            throw new InvalidParametersException("Invalid link: " + url);
        }
        var link = linkRepository.findByUrl(url);
        if (link == null) {
            linkRepository.add(new Link(0, url));
        }
        link = linkRepository.findByUrl(url);
        if (!chatLinkRepository.findAllByChatId(chat.id()).stream().map(Subscription::link).toList().contains(link)) {
            chatLinkRepository.add(new Subscription(chat, link));
        }
        return convert(link);
    }

    public LinkResponse removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        var chat = chatRepository.findById(tgChatId);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + tgChatId + " doesn't exist");
        }
        String url = removeLinkRequest.link().toString();
        var link = linkRepository.findByUrl(url);
        if (link == null) {
            throw new LinkNotFoundException("Link not found: " + url);
        }
        chatLinkRepository.remove(new Subscription(chat, link));
        return convert(link);
    }

    public LinkResponse convert(Link link) {
        try {
            return new LinkResponse(link.id(), new URI(link.url()));
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
