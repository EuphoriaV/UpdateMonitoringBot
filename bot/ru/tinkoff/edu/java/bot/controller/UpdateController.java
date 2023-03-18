package ru.tinkoff.edu.java.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.LinkUpdate;

import java.security.InvalidParameterException;

@RestController
@RequestMapping("/updates")
public class UpdateController {
    @PostMapping
    public void update(@RequestBody LinkUpdate linkUpdate) {
        //throw new InvalidParameterException("Invalid parameters");
    }
}
