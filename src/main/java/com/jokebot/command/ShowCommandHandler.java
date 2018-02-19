package com.jokebot.command;

import com.jokebot.bot.JokeBot;
import com.jokebot.model.JokeInMap;
import com.jokebot.model.NewUser;
import com.jokebot.model.ReceivedMessage;
import org.telegram.telegrambots.api.objects.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ShowCommandHandler implements CommandHandler {

    public static final String SHOW_CMD = "show";

    private final JokeBot jokeBot;

    public ShowCommandHandler(JokeBot jokeBot) {
        this.jokeBot = jokeBot;
    }

    @Override
    public boolean canHandle(Message message) {
        return SHOW_CMD.equalsIgnoreCase(message.getText());
    }

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        StringBuilder badBuilder = new StringBuilder();
        StringBuilder goodBuilder = new StringBuilder();
        ArrayList<Integer> minUsers = new ArrayList<>();
        ArrayList<Integer> maxUsers = new ArrayList<>();

        ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> chatMap = jokeBot.getChatMap();
        if (!chatMap.containsKey(chatId)) {
            jokeBot.sendMessage(message, messageId, "В данном чате еще никто \"Хуево\" не шутил");
        } else {
            if (chatMap.isEmpty()) {
                jokeBot.sendMessage(message, messageId, "список пуст");
            } else {
                Integer min = 0;
                Integer max = 0;
                HashMap<Integer, Integer> tmpMap = new HashMap<>();
                for (ReceivedMessage key : chatMap.get(chatId).keySet()){
                    if (tmpMap.containsKey(key.getId())){
                        tmpMap.put(key.getId(), tmpMap.get(key.getId()) + chatMap.get(chatId).get(key).getCount());
                    }
                    else {
                        tmpMap.put(key.getId(), chatMap.get(chatId).get(key).getCount());
                    }
                }
                for (Integer key : tmpMap.keySet()) {
                    if (tmpMap.get(key) <= min) {
                        min = tmpMap.get(key);
                    }
                }
                for (Integer key : tmpMap.keySet()) {
                    if (tmpMap.get(key) >= max) {
                        max = tmpMap.get(key);
                    }
                }
                for (Integer key : tmpMap.keySet()) {
                    if (tmpMap.get(key).equals(min)) {
                        minUsers.add(key);
                    }
                }
                for (Integer key : tmpMap.keySet()) {
                    if (tmpMap.get(key).equals(max)) {
                        maxUsers.add(key);
                    }
                }
                Map<Integer, NewUser> allUsers = jokeBot.getAllUsers();
                if (!minUsers.isEmpty() && !(min == 0)) {
                    jokeBot.sendMessage(message, messageId, "Звание \"Хуев шутник\" сегодня получает ");
                    for (Integer i : minUsers) {
                        badBuilder.append(allUsers.get(i).getFirstName() + " " + allUsers.get(i).getLastName() +
                                " \"" + allUsers.get(i).getUserName() + "\" рейтинг " + min + "\n");
                       // jokeBot.sendMsg(message, allUsers.get(i).getFirstName() + " " + allUsers.get(i).getLastName() +
                               // " \"" + allUsers.get(i).getUserName() + "\" рейтинг " + min);
                    }
                    jokeBot.sendMsg(message, badBuilder.toString());
                    minUsers.clear();
                }
                if (!maxUsers.isEmpty() && !(max == 0)) {
                    jokeBot.sendMessage(message, messageId, "Звание \"Шутник дня\" сегодня получает ");
                    for (Integer j : maxUsers) {
                        goodBuilder.append(allUsers.get(j).getFirstName() + " " + allUsers.get(j).getLastName() +
                                " \"" + allUsers.get(j).getUserName() + "\" рейтинг " + max + "\n");
                       // jokeBot.sendMsg(message, allUsers.get(j).getFirstName() + " " + allUsers.get(j).getLastName() +
                              //  " \"" + allUsers.get(j).getUserName() + "\" рейтинг " + max);
                    }
                    jokeBot.sendMsg(message, goodBuilder.toString());
                    maxUsers.clear();
                }
            }
        }
    }
}