package main;

import main.AnotherData.BotStrings;
import main.bot.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.*;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.util.*;

import main.bot.MessageOperations;

@SpringBootApplication
public class JokeBot extends TelegramLongPollingBot{


    public static HashMap<Long, HashMap<ReceivedMessage, JokeInMap>> chatMap = new HashMap<>();
    HashMap<Integer, NewUser> allUsers = new HashMap<>();
    MessageOperations messageOperations = new MessageOperations();

    public static void main(String[] args) throws TelegramApiException {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new JokeBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        SpringApplication.run(JokeBot.class, args);

        Calendar cal = Calendar.getInstance();
        Date date = new Date(117,8,7,23,59,59);
        cal.setTime(date);
       // Timer time = new Timer();
       // MapEraser st = new MapEraser();
       // time.scheduleAtFixedRate(st, cal.getTime(),86400000);
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


}
