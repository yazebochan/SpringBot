package com.jokebot.util;

import java.util.Map;
import java.util.TimerTask;

public class MapEraser extends TimerTask {
    private final Map map;

    public MapEraser(Map map) {
        this.map = map;
    }

    @Override
    public void run(){
        map.clear();
        System.out.println("erased");
    }
}
