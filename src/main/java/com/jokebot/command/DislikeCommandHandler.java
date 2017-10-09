package com.jokebot.command;

import com.jokebot.bot.JokeBot;
import com.jokebot.model.Joke;
import com.jokebot.model.JokeInMap;
import com.jokebot.model.NewUser;
import com.jokebot.model.ReceivedMessage;
import org.telegram.telegrambots.api.objects.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class DislikeCommandHandler implements CommandHandler{
    private static final String DISLIKE_MD = "-";

    private final JokeBot jokeBot;

    public DislikeCommandHandler(JokeBot jokeBot){
        this.jokeBot = jokeBot;
    }

    @Override
    public boolean canHandle(Message message){
        return message.isReply() && DISLIKE_MD.equals(message.getText());
    }

    @Override
    public void handle(Message message){
        ReceivedMessage newUser = new ReceivedMessage();
        newUser.setUserName(message.getReplyToMessage().getFrom().getUserName());
        newUser.setFirstName(message.getReplyToMessage().getFrom().getFirstName());
        newUser.setLastName(message.getReplyToMessage().getFrom().getLastName());
        newUser.setId(message.getReplyToMessage().getFrom().getId());
        newUser.setMessageId(message.getReplyToMessage().getMessageId());
        Joke joke = new Joke();
        joke.setMessageId(message.getReplyToMessage().getMessageId());
        joke.setReactedUser(message.getFrom().getId());

        NewUser newUser1 = new NewUser();
        newUser1.setUserName(message.getReplyToMessage().getFrom().getUserName());
        newUser1.setFirstName(message.getReplyToMessage().getFrom().getFirstName());
        newUser1.setLastName(message.getReplyToMessage().getFrom().getLastName());
        jokeBot.getAllUsers().put(message.getReplyToMessage().getFrom().getId(), newUser1);

        joke.setCount(-1);

        ConcurrentMap<Long, Map<ReceivedMessage, JokeInMap>> chatMap = jokeBot.getChatMap();
        if (chatMap.containsKey(message.getChatId()))  //check for the chat in chatMap
        {
            if (chatMap.get(message.getChatId()).containsKey(newUser)) {
                if (chatMap.get(message.getChatId()).get(newUser).getReactedUser().contains(joke.getReactedUser())  && chatMap.get(message.getChatId()).get(newUser).getCount().equals(-1)) {
                    jokeBot.sendMsg(message, "Уже минусовал это сообщение, хватит");
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
    }
}
