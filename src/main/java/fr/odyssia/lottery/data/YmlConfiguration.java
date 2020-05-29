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

    public Map<String, Object> getFragments() {
        return main.getConfig().getConfigurationSection("items").getValues(false);
    }

    public int getFragment(String fragment) {
        return (int) main.getConfig().getConfigurationSection("items").get(fragment);
    }
}
