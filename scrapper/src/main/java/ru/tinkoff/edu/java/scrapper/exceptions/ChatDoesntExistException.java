package ru.tinkoff.edu.java.scrapper.exceptions;

public class ChatDoesntExistException extends RuntimeException {
    public ChatDoesntExistException(String message) {
        super(message);
    }
}
