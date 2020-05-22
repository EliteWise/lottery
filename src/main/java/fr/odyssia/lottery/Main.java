package fr.odyssia.lottery;

import fr.odyssia.lottery.command.LotterySpawner;
import fr.odyssia.lottery.data.JsonRequest;
import fr.odyssia.lottery.data.YmlConfiguration;
import fr.odyssia.lottery.listener.Inventory;
import fr.odyssia.lottery.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public JsonRequest yamlRequest;
    public YmlConfiguration ymlConfigRequest;

    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();

        ymlConfigRequest = new YmlConfiguration(this);

        yamlRequest = new JsonRequest(this);
        yamlRequest.createFolder();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Inventory(this), this);

        getCommand(Constants.COMMAND_NAME).setExecutor(new LotterySpawner());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public String getPluginName() {
        return this.getDescription().getName();
    }
}
