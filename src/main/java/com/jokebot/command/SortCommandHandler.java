package com.jokebot.command;

import com.jokebot.bot.JokeBot;
import com.jokebot.model.JokeInMap;
import com.jokebot.model.NewUser;
import com.jokebot.model.ReceivedMessage;
import com.jokebot.util.MapOperations;
import org.telegram.telegrambots.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SortCommandHandler implements CommandHandler {

    public static final String SORT_CMD = "sort";

    private final JokeBot jokeBot;

    public SortCommandHandler(JokeBot jokeBot) {
        this.jokeBot = jokeBot;
    }

    @Override
    public boolean canHandle(Message message) {
        return SORT_CMD.equalsIgnoreCase(message.getText());
    }

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();

        Map<ReceivedMessage, JokeInMap> tmpMap = jokeBot.getChatMap().get(chatId);
        Map<Integer, Integer> finalMap = new HashMap<>();
        Map<Integer, Integer> finalMapOverZero = new HashMap<>();
        Map<Integer, Integer> finalMapBeyondZero = new HashMap<>();
        for (ReceivedMessage key : tmpMap.keySet()){
            if (finalMap.containsKey(key.getId())){
                finalMap.put(key.getId(), finalMap.get(key.getId()) + jokeBot.getChatMap().get(chatId).get(key).getCount());
            }
            else
                finalMap.put(key.getId(), tmpMap.get(key).getCount());
        }
        for (Integer key : finalMap.keySet()){
            if (finalMap.get(key) > 0){
                finalMapOverZero.put(key, finalMap.get(key));
            }
        }
        for (Integer key : finalMap.keySet()){
            if (finalMap.get(key) < 0){
                finalMapBeyondZero.put(key, finalMap.get(key));
            }
        }
        int n = 0;
        MapOperations mapOperations = new MapOperations();
        ArrayList<ReceivedMessage> sortedUsers = mapOperations.sortingKeys(finalMapBeyondZero);
        Map<Integer, NewUser> allUsers = jokeBot.getAllUsers();
        if (sortedUsers.size() > 0) {
            jokeBot.sendMessage(chatId, messageId, "Топ (5) хуевых шутников сегодня: ");
            if (sortedUsers.size() < 6)
                n = sortedUsers.size();
            else
                n = 5;
            for (int i = 0; i < n; i++) {
                jokeBot.sendMessage(chatId, allUsers.get(sortedUsers.get(i)).getFirstName() + " " + allUsers.get(sortedUsers.get(i)).getLastName() +
                        " \"" + allUsers.get(sortedUsers.get(i)).getUserName() + "\" рейтинг " +
                        mapOperations.sortingValues(finalMapBeyondZero).get(i).toString());
            }
            sortedUsers.clear();
        }
        ArrayList<ReceivedMessage> sortedUsersOverZero = mapOperations.sortingKeys(finalMapOverZero);
        if (sortedUsersOverZero.size() > 0) {
            jokeBot.sendMessage(chatId, messageId, "Топ (5) нормальных шутников сегодня: ");
            int tempI = sortedUsersOverZero.size() - 1;
            if (sortedUsersOverZero.size() < 6)
                n = sortedUsersOverZero.size();
            else
                n = 5;
            for (int i = 0; i < n; i++) {
                jokeBot.sendMessage(chatId, allUsers.get(sortedUsersOverZero.get(tempI)).getFirstName() + " " + allUsers.get(sortedUsersOverZero.get(tempI)).getLastName() +
                        " \"" + allUsers.get(sortedUsersOverZero.get(tempI)).getUserName() + "\" рейтинг " +
                        mapOperations.sortingValues(finalMapOverZero).get(tempI).toString());
                tempI--;
            }
            sortedUsersOverZero.clear();
        }
    }
}
