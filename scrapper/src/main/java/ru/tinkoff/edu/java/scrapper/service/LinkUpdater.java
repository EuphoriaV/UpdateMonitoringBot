package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.database.dto.Link;
import ru.tinkoff.edu.java.scrapper.database.dto.Subscription;

import java.util.List;

public interface LinkUpdater {
    List<Subscription> getUpdates();

    void updateAll(List<Link> links);
}
