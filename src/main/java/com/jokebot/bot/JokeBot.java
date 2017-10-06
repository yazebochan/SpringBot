package com.jokebot.bot;

import com.jokebot.util.MessageOperations;
import com.jokebot.model.JokeInMap;
import com.jokebot.model.NewUser;
import com.jokebot.model.ReceivedMessage;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JokeBot extends TelegramLongPollingBot{
    private final String botToken;
    private final String botUsername;

    private final ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> chatMap;
    private final Map<Integer, NewUser> allUsers;
    private final MessageOperations messageOperations;

    public JokeBot(String botToken, String botUsername) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.chatMap = new ConcurrentHashMap<>();
        this.allUsers = new HashMap<>();
        this.messageOperations = new MessageOperations(this);
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
        NewUser newUser = new NewUser();
        try {
            if (message.isReply() && message.getText().equals("-")) {
                messageOperations.messageEqualsMinus(update, newUser, chatMap, allUsers);
            }

            if (message.isReply() && message.getText().equals("+")){
                messageOperations.messageEqualsPlus(update, newUser, chatMap, allUsers);
            } else if (message.getText().equals("show")){
                messageOperations.messsageEqualsShow(update, chatMap, allUsers);
            }

            if (message.getText().equals("sort")) {
                messageOperations.messageEqualsSort(update, chatMap, allUsers);
            }
        } catch (NullPointerException e) {
            // WTF
        }
    }

    public void clearChatMap(){
        this.chatMap.clear();
    }

    public void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(Message message, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(message.getChatId().toString());
        sendMsg.setReplyToMessageId(message.getMessageId());
        sendMsg.setText(text);
        try {
            sendMessage(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
