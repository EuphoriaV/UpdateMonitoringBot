package ru.tinkoff.edu.java.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;
import ru.tinkoff.edu.java.bot.service.BotService;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    private final BotService botService;

    public UpdateController(BotService botService) {
        this.botService = botService;
    }

    @PostMapping
    public void update(@RequestBody LinkUpdate linkUpdate) {
        botService.sendUpdate(linkUpdate);
    }
}
