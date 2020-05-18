package fr.odyssia.lottery;

import fr.odyssia.lottery.command.LotterySpawner;
import fr.odyssia.lottery.data.YamlRequest;
import fr.odyssia.lottery.listener.Inventory;
import fr.odyssia.lottery.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public YamlRequest yamlRequest;

    @Override
    public void onEnable() {
        // Plugin startup logic
        yamlRequest = new YamlRequest(this);
        yamlRequest.createFile();

        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new Inventory(), this);

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
