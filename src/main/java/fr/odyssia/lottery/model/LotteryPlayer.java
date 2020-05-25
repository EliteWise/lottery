package fr.odyssia.lottery.model;

import java.util.Map;

public class LotteryPlayer {

    private String name;
    private int tokens;
    private Map<String, Object> fragments;

    public LotteryPlayer(String name, int tokens, Map<String, Object> fragments) {
        this.name = name;
        this.tokens = tokens;
        this.fragments = fragments;
    }

    public String getName() {
        return name;
    }

    public int getTokens() {
        return tokens;
    }

    public Map<String, Object> getFragments() {
        return fragments;
    }
}
