package main.bot;

import java.util.ArrayList;

public class JokeInMap {
    private Integer messageId;
    private Integer count;
    private ArrayList<Integer> reactedUser = new ArrayList<>();

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<Integer> getReactedUser() {
        return reactedUser;
    }

    public void setReactedUser(Integer reactedUser) {
        this.reactedUser.add(reactedUser);
    }

    @Override
    public String toString() {
        return "JokeInMap{" +
                "messageId=" + messageId +
                ", count=" + count +
                ", reactedUser=" + reactedUser +
                '}';
    }
}
