package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

public abstract class Bot implements UpdatesListener {

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            process(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    //Боту будет приходить команда /commandName, и ее будет обрабатывать соответствующий метод с именем commandName
    //Если такого метода нет, то будет вызван метод обрабатывающий невалидные команды
    public void process(Update update) {
        if (update.message() == null) {
            return;
        }
        Message message = update.message();
        if (message.text() == null) {
            handleInvalidMessage(message.chat().id(), "");
            return;
        }
        Method[] methods = this.getClass().getDeclaredMethods();
        String messageText = message.text();
        long chatId = message.chat().id();
        for (Method method : methods) {
            if (!Modifier.isPublic(method.getModifiers())) {
                continue;
            }
            if (method.getName().equals(messageText.substring(1))) {
                try {
                    method.invoke(this, chatId);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.println(e.getMessage());
                }
                return;
            }
        }
        handleInvalidMessage(chatId, messageText);
    }

    abstract SendResponse handleInvalidMessage(long chatId, String text);
}
