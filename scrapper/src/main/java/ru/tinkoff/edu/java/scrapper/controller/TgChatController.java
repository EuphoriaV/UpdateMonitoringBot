package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@RequestMapping("/tg-chat/{id}")
@RestController
public class TgChatController {
    private final ChatService chatService;

    public TgChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public void createChat(@PathVariable long id, @RequestHeader String username) {
        chatService.registerChat(id, username);
    }

    @DeleteMapping
    public void deleteChat(@PathVariable long id) {
        chatService.unregisterChat(id);
    }
}
