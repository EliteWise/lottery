package fr.odyssia.lottery;

import fr.odyssia.lottery.data.YamlRequest;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public YamlRequest yamlRequest;

    @Override
    public void onEnable() {
        // Plugin startup logic
        yamlRequest = new YamlRequest(this);
        yamlRequest.createFile();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getPluginName() {
        return this.getDescription().getName();
    }
}
