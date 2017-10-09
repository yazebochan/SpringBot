
package com.jokebot;

import com.jokebot.bot.JokeBot;
import com.jokebot.util.BotStrings;
import com.jokebot.bot.JokeBot;
import com.jokebot.util.MapEraser;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class Application {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        JokeBot jokeBot = new JokeBot(BotStrings.botToken, BotStrings.botUserName);
        try {
            telegramBotsApi.registerBot(jokeBot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        Integer day = new Date().getDay()+1;
        Calendar cal = Calendar.getInstance();
        Date date = new Date(117,9,day,23,59,59);
        cal.setTime(date);
        Timer time = new Timer();
        MapEraser st = new MapEraser(jokeBot.getChatMap());

        time.scheduleAtFixedRate(st, cal.getTime(),86400000);
    }
}
