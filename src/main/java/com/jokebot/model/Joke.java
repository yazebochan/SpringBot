package com.jokebot.model;

public class Joke {
    private Integer messageId;
    private Integer count;
    private Integer reactedUser;

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

    public Integer getReactedUser() {
        return reactedUser;
    }

    public void setReactedUser(Integer reactedUser) {
        this.reactedUser = reactedUser;
    }
}
