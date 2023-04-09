package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.exceptions.ChatDoesntExistException;
import ru.tinkoff.edu.java.bot.exceptions.InvalidParametersException;

@RequestMapping("/tg-chat/{id}")
@RestController
public class TgChatController {
    @PostMapping
    public void createChat(@PathVariable long id) {
        //throw new InvalidParametersException("Invalid parameters");
    }

    @DeleteMapping
    public void deleteChat(@PathVariable long id) {
        //throw new InvalidParametersException("Invalid parameters");
        //throw new ChatDoesntExistException("Chat doesn't exist");
    }
}
