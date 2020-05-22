package fr.odyssia.lottery.model;

public class LotteryPlayer {

    private String name;
    private int tokens;

    public LotteryPlayer(String name, int tokens) {
        this.name = name;
        this.tokens = tokens;
    }

    public String getName() {
        return name;
    }

    public int getTokens() {
        return tokens;
    }
}
