package com.jokebot.command;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Random;

public class JokesCommandHandler {
    public String joke() {
        Random rnd = new Random();
        Integer jokesCount = 0;
        String joke = "ooops, error";
        try {
            Document doc = Jsoup.connect("https://www.anekdot.ru/").get();
            Elements elements = doc.getElementsByClass("text");
            for (Element key : elements){
                jokesCount++;
            }
            int randomNum = rnd.nextInt((jokesCount)) + 1;
            Element element = elements.get(randomNum);
            joke = element.ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return joke;
    }
}
