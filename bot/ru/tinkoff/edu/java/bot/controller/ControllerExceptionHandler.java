package ru.tinkoff.edu.java.bot.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.tinkoff.edu.java.bot.dto.ApiErrorResponse;

import java.security.InvalidParameterException;
import java.util.Arrays;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(InvalidParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse badRequest(InvalidParameterException exception) {
        return new ApiErrorResponse("Invalid parameters", HttpStatus.BAD_REQUEST.toString(),
                exception.getClass().getName(), exception.getMessage(),
                Arrays.stream(exception.getStackTrace()).map(StackTraceElement::toString).toList());
    }
}
