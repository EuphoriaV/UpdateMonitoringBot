package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.scrapper.dto.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exceptions.ChatDoesntExistException;
import ru.tinkoff.edu.java.scrapper.exceptions.InvalidParametersException;
import ru.tinkoff.edu.java.scrapper.exceptions.LinkNotFoundException;

import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(InvalidParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse invalidParameters(InvalidParametersException exception) {
        return new ApiErrorResponse("Invalid parameters", HttpStatus.BAD_REQUEST.toString(),
                exception.getClass().getName(), exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList());
    }

    @ExceptionHandler(LinkNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse linkNotFound(LinkNotFoundException exception) {
        return new ApiErrorResponse("Link not found", HttpStatus.NOT_FOUND.toString(),
                exception.getClass().getName(), exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList());
    }

    @ExceptionHandler(ChatDoesntExistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse chatDoesntExist(ChatDoesntExistException exception) {
        return new ApiErrorResponse("Chat doesn't exist", HttpStatus.NOT_FOUND.toString(),
                exception.getClass().getName(), exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList());
    }
}
