package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.link_parser.LinkParser;
import ru.tinkoff.edu.java.scrapper.database.entity.Link;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.database.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.exceptions.ChatDoesntExistException;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidParametersException;
import ru.tinkoff.edu.java.scrapper.exceptions.LinkNotFoundException;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.ArrayList;

@Service
@Primary
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository jpaLinkRepository;
    private final JpaChatRepository jpaChatRepository;

    public JpaLinkService(JpaLinkRepository jpaLinkRepository, JpaChatRepository jpaChatRepository) {
        this.jpaLinkRepository = jpaLinkRepository;
        this.jpaChatRepository = jpaChatRepository;
    }

    public ListLinksResponse findAll(long tgChatId) {
        var chat = jpaChatRepository.findById(tgChatId).orElse(null);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + tgChatId + " doesn't exist");
        }
        var list = chat.getLinks().stream().map(this::convert).toList();
        return new ListLinksResponse(list, list.size());
    }

    public LinkResponse addLink(long tgChatId, AddLinkRequest addLinkRequest) {
        var chat = jpaChatRepository.findById(tgChatId).orElse(null);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + tgChatId + " doesn't exist");
        }
        String url = addLinkRequest.link().toString();
        var parseRes = LinkParser.parseLink(url);
        if (parseRes == null) {
            throw new InvalidParametersException("Invalid link: " + url);
        }
        var link = jpaLinkRepository.findByUrl(url);
        if (link == null) {
            link = new Link();
            link.setUrl(url);
            link.setCheckedAt(OffsetDateTime.now());
            jpaLinkRepository.save(link);
            link = jpaLinkRepository.findByUrl(url);
        }
        if (link.getChats() == null) {
            link.setChats(new ArrayList<>());
        }
        if (chat.getLinks() == null) {
            chat.setLinks(new ArrayList<>());
        }
        if (!link.getChats().contains(chat)) {
            link.getChats().add(chat);
            chat.getLinks().add(link);
        }
        jpaLinkRepository.save(link);
        jpaChatRepository.save(chat);
        return convert(link);
    }

    public LinkResponse removeLink(long tgChatId, RemoveLinkRequest removeLinkRequest) {
        var chat = jpaChatRepository.findById(tgChatId).orElse(null);
        if (chat == null) {
            throw new ChatDoesntExistException("Chat with id " + tgChatId + " doesn't exist");
        }
        String url = removeLinkRequest.link().toString();
        var link = jpaLinkRepository.findByUrl(url);
        if (link == null) {
            throw new LinkNotFoundException("Link not found: " + url);
        }
        link.getChats().remove(chat);
        chat.getLinks().remove(link);
        jpaChatRepository.save(chat);
        jpaLinkRepository.save(link);
        return convert(link);
    }

    public LinkResponse convert(Link link) {
        try {
            return new LinkResponse(link.getId(), new URI(link.getUrl()));
        } catch (URISyntaxException e) {
            return null;
        }
    }
}
