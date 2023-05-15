package ru.tinkoff.edu.java.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import ru.tinkoff.edu.java.bot.annotation.Command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Bot implements UpdatesListener {

    @Override
    public int process(List<Update> list) {
        for (Update update : list) {
            process(update);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /*
    Боту будет приходить команда /commandName, и ее будет обрабатывать соответствующий
    метод с анотацией Command с полем name равным commandName
    Если такого метода нет, то будет вызван метод, обрабатывающий невалидные команды
     */
    public void process(Update update) {
        if (update.message() == null) {
            return;
        }
        Message message = update.message();
        if (message.text() == null) {
            handleInvalidMessage(message);
            return;
        }
        String messageText = message.text();
        for (Method method : getCommandHandlers()) {
            var command = method.getAnnotation(Command.class);
            String name = command.name();
            if (messageText.startsWith(name)) {
                try {
                    method.invoke(this, message);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    System.err.println(e.getMessage());
                }
                return;
            }
        }
        handleInvalidMessage(message);
    }

    protected List<Method> getCommandHandlers() {
        List<Method> list = new ArrayList<>();
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Command.class)) {
                list.add(method);
            }
        }
        return list;
    }

    abstract SendResponse handleInvalidMessage(Message message);
}
