package main;

import main.AnotherData.BotStrings;
import main.bot.*;
import org.telegram.telegrambots.*;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.*;

import main.bot.MessageOperations;

public class JokeBot extends TelegramLongPollingBot{

    public static JokeBot jokeBot = getJokeBot();
    private static JokeBot getJokeBot(){
        ApiContextInitializer.init();
        if (jokeBot == null){
            jokeBot = new JokeBot();
        }
        return jokeBot;
    }

    public static HashMap<Long, HashMap<ReceivedMessage, JokeInMap>> chatMap = new HashMap<>();
    HashMap<Integer, NewUser> allUsers = new HashMap<>();
    MessageOperations messageOperations = new MessageOperations(jokeBot);

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new JokeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        Integer day = new Date().getDay()+1;
        Calendar cal = Calendar.getInstance();
        Date date = new Date(117,9,day,23,59,59);
        cal.setTime(date);
        Timer time = new Timer();
        MapEraser st = new MapEraser();
        time.scheduleAtFixedRate(st, cal.getTime(),86400000);
    }



    @Override
    public String getBotUsername() {
        return BotStrings.botUserName;
    }

    @Override
    public String getBotToken() {
        return BotStrings.botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Update msgUpdate = update;
        Message message = update.getMessage();
        NewUser newUser1 = new NewUser();
        try {
            if (message.isReply() && message.getText().equals("-"))
            {
                messageOperations.messageEqualsMinus(update, newUser1, chatMap, allUsers);
            }
            if (message.isReply() && message.getText().equals("+")){
                messageOperations.messageEqualsPlus(update, newUser1, chatMap, allUsers);
            }


             else if (message.getText().equals("show")){
                messageOperations.messsageEqualsShow(update, chatMap, allUsers);
            }

            if (message.getText().equals("sort")) {
                messageOperations.messageEqualsSort(update, chatMap, allUsers);
            }
        } catch (NullPointerException e) {
        }
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
