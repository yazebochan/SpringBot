package com.jokebot.util;

import com.jokebot.bot.JokeBot;

import java.util.TimerTask;

public class MapEraser extends TimerTask {
    private final JokeBot jokeBot;

    public MapEraser(JokeBot jokeBot) {
        this.jokeBot = jokeBot;
    }

    @Override
    public void run(){
        jokeBot.clearChatMap();
        System.out.println("erased");
    }
}
