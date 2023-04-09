package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.exceptions.LinkNotFoundException;
import ru.tinkoff.edu.java.bot.exceptions.InvalidParametersException;

@RestController
@RequestMapping("/links")
public class LinksController {
    @GetMapping
    public ListLinksResponse getLinks(@RequestHeader long tgChatId) {
        //throw new InvalidParametersException("Invalid parameters");
        return null;
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader long tgChatId, @RequestBody AddLinkRequest addLinkRequest) {
        //throw new InvalidParametersException("Invalid parameters");
        return null;
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader long tgChatId, @RequestBody RemoveLinkRequest removeLinkRequest) {
        //throw new InvalidParametersException("Invalid parameters");
        //throw new LinkNotFoundException("Link not found");
        return new LinkResponse(tgChatId, removeLinkRequest.link());
    }
}
