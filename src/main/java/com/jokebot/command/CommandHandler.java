package com.jokebot.command;

import org.telegram.telegrambots.api.objects.Message;

public interface CommandHandler {
    boolean canHandle(Message message);
    void handle(Message message);
}
