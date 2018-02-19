package com.jokebot.bot;

import com.jokebot.command.*;
import com.jokebot.model.*;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.jokebot.util.BotStrings.botUserName;

public class JokeBot extends TelegramLongPollingBot{
    public static final int ADMIN_CHAT_ID = 3423564;
    private final String botToken;
    private final String botUsername;

    private final ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> chatMap;
    private final Map<Integer, NewUser> allUsers;
    private final List<CommandHandler> commandHandlers;

    public JokeBot(String botToken, String botUsername){
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
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        JokesCommandHandler jokesCommandHandler = new JokesCommandHandler();
        Message message = update.getMessage();
        for (CommandHandler commandHandler : commandHandlers){
            if(commandHandler.canHandle(message)){
                commandHandler.handle(message);
                break;
            }
        }
        if ((message.getText().equals("+") && message.isReply() || message.getText().equals("-") && message.isReply())){
            String firstName = message.getFrom().getFirstName();
            String lastName = message.getFrom().getLastName();
            String userName = message.getFrom().getUserName();
            String chatTitle = message.getChat().getTitle();
            Long chatId = message.getChatId();

            sendStatMessage(message, ADMIN_CHAT_ID,
                    "userFirstName = " + firstName + "\n" +
                            "userLastName = " + lastName + "\n" +
                            "userName = " + userName + "\n" +
                            "chatId = " + chatId + "\n" +
                            "chatTitle = " + chatTitle);
        }
        if (message.getText().equals("joke")){
            sendMsg(message, jokesCommandHandler.joke());
        }
    }

    public ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> getChatMap() {
        return chatMap;
    }

    public Map<Integer, NewUser> getAllUsers() {
        return allUsers;
    }
    public void sendMsg(Message message, String text) {
        sendMessage(message, null, text);
    }

    public void sendMessage(Message message, Integer replyToId, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(message.getChatId().toString());
        sendMsg.setReplyToMessageId(replyToId);
        sendMsg.setText(text);
        try {
            sendMessage(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendStatMessage(Message message, Integer  id, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(id.toString());
        sendMsg.setText(text);
        try {
            sendMessage(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
