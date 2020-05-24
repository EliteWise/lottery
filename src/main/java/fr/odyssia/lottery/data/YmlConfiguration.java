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
        return main.getConfig().getInt("animation_duration");
    }

    public int getAnimationSpeed() {
        return main.getConfig().getInt("animation_speed");
    }

    public Set<String> getItems() {
        return main.getConfig().getConfigurationSection("items").getKeys(false);
    }

    public Map<String, Object> getFragments() {
        Map<String, Object> map = main.getConfig().getConfigurationSection("items").getValues(false);
        Map<String, Object> linkedHashMap = new LinkedHashMap<>(map);
        return linkedHashMap;
    }
}
