package com.jokebot.bot;

import com.jokebot.command.*;
import com.jokebot.model.JokeInMap;
import com.jokebot.model.NewUser;
import com.jokebot.model.ReceivedMessage;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JokeBot extends TelegramLongPollingBot{
    private final String botToken;
    private final String botUsername;

    private final ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> chatMap;
    private final Map<Integer, NewUser> allUsers;
    private final List<CommandHandler> commandHandlers;

    public JokeBot(String botToken, String botUsername) {
        this.allUsers = new HashMap<>();
        this.chatMap = new ConcurrentHashMap<>();
        
        this.botToken = botToken;
        this.botUsername = botUsername;
        
        this.commandHandlers = new ArrayList<>();
        commandHandlers.add(new DislikeCommandHandler(this));
        commandHandlers.add(new LikeCommandHandler(this));
        commandHandlers.add(new SortCommandHandler(this));
        commandHandlers.add(new ShowCommandHandler(this));
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        for (CommandHandler commandHandler : commandHandlers) {
            if (commandHandler.canHandle(message)) {
                commandHandler.handle(message);
                break;
            }
        }
    }

    public ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> getChatMap() {
        return chatMap;
    }

    public Map<Integer, NewUser> getAllUsers() {
        return allUsers;
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(chatId, null, text);
    }

    public void sendMessage(Long chatId, Integer replyToId, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(chatId.toString());
        sendMsg.setReplyToMessageId(replyToId);
        sendMsg.setText(text);
        try {
            sendMessage(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
