package com.jokebot.command;

import com.jokebot.bot.JokeBot;
import com.jokebot.model.Joke;
import com.jokebot.model.JokeInMap;
import com.jokebot.model.NewUser;
import com.jokebot.model.ReceivedMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class LikeCommandHandler implements CommandHandler {

    public static final String LIKE_CMD = "+";

    private final JokeBot jokeBot;

    public LikeCommandHandler(JokeBot jokeBot) {
        this.jokeBot = jokeBot;
    }

    @Override
    public boolean canHandle(Message message) {
        return message.isReply() && LIKE_CMD.equals(message.getText());
    }

    @Override
    public void handle(Message message) {
        Long chatId = message.getChatId();
        Integer messageId = message.getMessageId();
        User replyToUser = message.getReplyToMessage().getFrom();

        ReceivedMessage newUser = new ReceivedMessage();
        newUser.setUserName(replyToUser.getUserName());
        newUser.setFirstName(replyToUser.getFirstName());
        newUser.setLastName(replyToUser.getLastName());
        newUser.setId(replyToUser.getId());
        newUser.setMessageId(message.getReplyToMessage().getMessageId());
        Joke joke = new Joke();
        joke.setMessageId(message.getReplyToMessage().getMessageId());
        joke.setReactedUser(message.getFrom().getId());

        NewUser newUser1 = new NewUser();
        newUser1.setUserName(replyToUser.getUserName());
        newUser1.setFirstName(replyToUser.getFirstName());
        newUser1.setLastName(replyToUser.getLastName());
        jokeBot.getAllUsers().put(replyToUser.getId(), newUser1);

        joke.setCount(1);
        ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> chatMap = jokeBot.getChatMap();
        if (message.getFrom().getId().equals(joke.getMessageId())) {
            jokeBot.sendMsg(message, "Попахивает накруткой, нельзя!");
        }
        else {
            if (chatMap.containsKey(chatId)) { //check for the chat in chatMap
                if (chatMap.get(chatId).containsKey(newUser)) {
                    if (chatMap.get(chatId).get(newUser).getReactedUser().contains(joke.getReactedUser()) &&
                            chatMap.get(chatId).get(newUser).getCount().equals(1)) {
                        jokeBot.sendMessage(message, messageId, "Уже плюсовал это сообщение, шутка норм, согласен");
                    } else {
                        Integer i = chatMap.get(chatId) //get innerMap
                                .get(newUser).getCount(); //get number of "Jokes" for currently user
                        chatMap.get(chatId).get(newUser).setReactedUser(message.getFrom().getId());
                        chatMap.get(chatId).get(newUser).setCount(i + 1);

                    }
                } else {
                    JokeInMap jokeInMap = new JokeInMap();
                    jokeInMap.setCount(joke.getCount());
                    jokeInMap.setMessageId(joke.getMessageId());
                    jokeInMap.setReactedUser(joke.getReactedUser());
                    chatMap.get(chatId).put(newUser, jokeInMap);
                }
            } else {
                JokeInMap jokeInMap = new JokeInMap();
                jokeInMap.setCount(joke.getCount());
                jokeInMap.setMessageId(joke.getMessageId());
                jokeInMap.setReactedUser(joke.getReactedUser());
                chatMap.put(chatId, new HashMap<>());
                chatMap.get(chatId).put(newUser, jokeInMap);
            }
        }
    }
}