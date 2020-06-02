package fr.odyssia.lottery.data;

import fr.odyssia.lottery.Main;

import java.util.*;

public class YmlConfiguration {

    private Main main;

    public YmlConfiguration(Main main) {
        this.main = main;
    }

    public YmlConfiguration() {}

    public int getAnimationDuration() {
        return main.getConfig().getInt("animation-duration");
    }

    public int getAnimationSpeed() {
        return main.getConfig().getInt("animation-speed");
    }

    public String getNpcName() { return main.getConfig().getString("npc-name"); }

    public String getProfession() { return main.getConfig().getString("npc-profession", "LIBRARIAN").toUpperCase(); }

    public Set<String> getItems() {
        return main.getConfig().getConfigurationSection("items").getKeys(false);
    }

    public String getTokenType() { return main.getConfig().getString("token-type"); }

    public String getFragmentMessage(String fragment) {
        return main.getConfig().getString("fragment-message").replace("{FRAGMENT}", fragment.replace("_", " ")).replace("&", "§");
    }

    public String getRewardMessage(String reward) {
        return main.getConfig().getString("reward-message").replace("{REWARD}", reward.replace("_", " ")).replace("&", "§");
    }

    public boolean enableSounds() { return main.getConfig().getBoolean("enable-sounds"); }

    public String getParticlesType() { return main.getConfig().getString("particles.type"); }

    public int getParticlesNumber() { return main.getConfig().getInt("particles.number"); }

    public float getParticlesSpeed() { return (float) main.getConfig().getDouble("particles.speed"); }

    public Map<String, Object> getFragments() {
        return main.getConfig().getConfigurationSection("items").getValues(false);
    }

    public int getFragment(String fragment) {
        return (int) main.getConfig().getConfigurationSection("items").get(fragment);
    }
}
