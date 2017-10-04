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
    /*public void messageEqualsMinus(Update update, NewUser newUser1){
            Message message = update.getMessage();
            ReceivedMessage newUser = new ReceivedMessage();
            newUser.setUserName(message.getReplyToMessage().getFrom().getUserName());
            newUser.setFirstName(message.getReplyToMessage().getFrom().getFirstName());
            newUser.setLastName(message.getReplyToMessage().getFrom().getLastName());
            newUser.setId(message.getReplyToMessage().getFrom().getId());
            newUser.setMessageId(message.getReplyToMessage().getMessageId());
            Joke joke = new Joke();
            joke.setMessageId(message.getReplyToMessage().getMessageId());
            joke.setReactedUser(message.getFrom().getId());

            newUser1.setUserName(message.getReplyToMessage().getFrom().getUserName());
            newUser1.setFirstName(message.getReplyToMessage().getFrom().getFirstName());
            newUser1.setLastName(message.getReplyToMessage().getFrom().getLastName());
            allUsers.put(message.getReplyToMessage().getFrom().getId(), newUser1);

            joke.setCount(-1);
            if (chatMap.containsKey(message.getChatId()))  //check for the chat in chatMap
            {
                if (chatMap.get(message.getChatId()).containsKey(newUser)) {
                    if (chatMap.get(message.getChatId()).get(newUser).getReactedUser().contains(joke.getReactedUser())) {
                        sendMsg(message, "Уже минусовал это сообщение, хватит");
                    } else {
                        Integer i = chatMap.get(message.getChatId()) //get innerMap
                                .get(newUser).getCount(); //get number of "Jokes" for currently user
                        chatMap.get(message.getChatId()).get(newUser).setReactedUser(message.getFrom().getId());
                        chatMap.get(message.getChatId()).get(newUser).setCount(i - 1);

                    }
                } else {
                    JokeInMap jokeInMap = new JokeInMap();
                    jokeInMap.setCount(joke.getCount());
                    jokeInMap.setMessageId(joke.getMessageId());
                    jokeInMap.setReactedUser(joke.getReactedUser());
                    chatMap.get(message.getChatId()).put(newUser, jokeInMap);
                }
            }

            else
            {
                JokeInMap jokeInMap = new JokeInMap();
                jokeInMap.setCount(joke.getCount());
                jokeInMap.setMessageId(joke.getMessageId());
                jokeInMap.setReactedUser(joke.getReactedUser());
                chatMap.put(message.getChatId(), new HashMap<ReceivedMessage, JokeInMap>());
                chatMap.get(message.getChatId()).put(newUser, jokeInMap);
            }
            System.out.println("map " + chatMap);

    }
    private void messageEqualsPlus(Update update, NewUser newUser1){
        {
            Message message = update.getMessage();
            ReceivedMessage newUser = new ReceivedMessage();
            newUser.setUserName(message.getReplyToMessage().getFrom().getUserName());
            newUser.setFirstName(message.getReplyToMessage().getFrom().getFirstName());
            newUser.setLastName(message.getReplyToMessage().getFrom().getLastName());
            newUser.setId(message.getReplyToMessage().getFrom().getId());
            newUser.setMessageId(message.getReplyToMessage().getMessageId());
            Joke joke = new Joke();
            joke.setMessageId(message.getReplyToMessage().getMessageId());
            joke.setReactedUser(message.getFrom().getId());

            newUser1.setUserName(message.getReplyToMessage().getFrom().getUserName());
            newUser1.setFirstName(message.getReplyToMessage().getFrom().getFirstName());
            newUser1.setLastName(message.getReplyToMessage().getFrom().getLastName());
            allUsers.put(message.getReplyToMessage().getFrom().getId(), newUser1);

            joke.setCount(1);
            if (chatMap.containsKey(message.getChatId()))  //check for the chat in chatMap
            {
                if (chatMap.get(message.getChatId()).containsKey(newUser)) {
                    if (chatMap.get(message.getChatId()).get(newUser).getReactedUser().contains(joke.getReactedUser()) && chatMap.get(message.getChatId()).get(newUser).getCount().equals(1)) {
                        sendMsg(message, "Уже плюсовал это сообщение, шутка норм, согласен");
                    } else {
                        Integer i = chatMap.get(message.getChatId()) //get innerMap
                                .get(newUser).getCount(); //get number of "Jokes" for currently user
                        chatMap.get(message.getChatId()).get(newUser).setReactedUser(message.getFrom().getId());
                        chatMap.get(message.getChatId()).get(newUser).setCount(i + 1);

                    }
                } else {
                    JokeInMap jokeInMap = new JokeInMap();
                    jokeInMap.setCount(joke.getCount());
                    jokeInMap.setMessageId(joke.getMessageId());
                    jokeInMap.setReactedUser(joke.getReactedUser());
                    chatMap.get(message.getChatId()).put(newUser, jokeInMap);
                }
            }

            else
            {
                JokeInMap jokeInMap = new JokeInMap();
                jokeInMap.setCount(joke.getCount());
                jokeInMap.setMessageId(joke.getMessageId());
                jokeInMap.setReactedUser(joke.getReactedUser());
                chatMap.put(message.getChatId(), new HashMap<ReceivedMessage, JokeInMap>());
                chatMap.get(message.getChatId()).put(newUser, jokeInMap);
            }
        }
    }

    public void messsageEqualsShow(Update update){
        Message message = update.getMessage();
        ArrayList<Integer> minUsers = new ArrayList<>();
        ArrayList<Integer> maxUsers = new ArrayList<>();

        if (!chatMap.containsKey(message.getChatId())) {
            sendMsg(message, "В данном чате еще никто \"Хуево\" не шутил");
        } else {
            if (chatMap.isEmpty()) {
                sendMsg(message, "список пуст");
            } else {
                Integer min = 0;
                Integer max = 0;
                HashMap<Integer, Integer> tmpMap = new HashMap<>();
                for (ReceivedMessage key : chatMap.get(message.getChatId()).keySet()){
                    if (tmpMap.containsKey(key.getId())){
                        tmpMap.put(key.getId(), tmpMap.get(key.getId()) + chatMap.get(message.getChatId()).get(key).getCount());
                    }
                    else {
                        tmpMap.put(key.getId(), chatMap.get(message.getChatId()).get(key).getCount());
                    }
                }
                System.out.println(tmpMap);
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
                System.out.println(maxUsers);
                {
                    sendMsg(message, "Звание \"Хуев шутник\" сегодня получает ");
                    for (Integer i : minUsers) {
                        sendMessage(message, allUsers.get(i).getFirstName() + " " + allUsers.get(i).getLastName() +
                                " @" + allUsers.get(i).getUserName() + " рейтинг " + min);
                    }
                    sendMsg(message, "Звание \"Шутник дня\" сегодня получает ");
                    for (Integer j : maxUsers) {
                        sendMessage(message, allUsers.get(j).getFirstName() + " " + allUsers.get(j).getLastName() +
                                " @" + allUsers.get(j).getUserName() + " рейтинг " + max);
                    }
                    maxUsers.clear();
                }
            }
        }
    }
    public void messageEqualsSort(Update update){
        Message message = update.getMessage();
            HashMap<ReceivedMessage, JokeInMap> tmpMap = chatMap.get(message.getChatId());
            HashMap<Integer, Integer> finalMap = new HashMap<>();
            HashMap<Integer, Integer> finalMapOverZero = new HashMap<>();
            HashMap<Integer, Integer> finalMapBeyondZero = new HashMap<>();
            for (ReceivedMessage key : tmpMap.keySet()){
                if (finalMap.containsKey(key.getId())){
                    finalMap.put(key.getId(), finalMap.get(key.getId())+1);
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
            if (sortedUsers.size() > 0) {
                sendMsg(message, "Топ хуевых шутников сегодня: ");
                if (sortedUsers.size() < 6)
                    n = sortedUsers.size();
                else
                    n = 5;
                for (int i = 0; i < n; i++) {
                    sendMessage(message, allUsers.get(sortedUsers.get(i)).getFirstName() + " " + allUsers.get(sortedUsers.get(i)).getLastName() +
                            " @" + allUsers.get(sortedUsers.get(i)).getUserName() + " рейтинг " +
                            mapOperations.sortingValues(finalMapBeyondZero).get(i).toString());
                }
                sortedUsers.clear();
            }
            ArrayList<ReceivedMessage> sortedUsersOverZero = mapOperations.sortingKeys(finalMapOverZero);
            if (sortedUsersOverZero.size() > 0) {
                sendMsg(message, "Топ нормальных шутников сегодня: ");
                int tempI = sortedUsersOverZero.size() - 1;
                if (sortedUsersOverZero.size() < 6)
                    n = sortedUsersOverZero.size();
                else
                    n = 5;
                for (int i = 0; i < n; i++) {
                    sendMessage(message, allUsers.get(sortedUsersOverZero.get(tempI)).getFirstName() + " " + allUsers.get(sortedUsersOverZero.get(tempI)).getLastName() +
                            " @" + allUsers.get(sortedUsersOverZero.get(tempI)).getUserName() + " рейтинг " +
                            mapOperations.sortingValues(finalMapOverZero).get(tempI).toString());
                    tempI--;
                }
                sortedUsersOverZero.clear();
            }

    }*/

    protected void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected void sendMessage(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
