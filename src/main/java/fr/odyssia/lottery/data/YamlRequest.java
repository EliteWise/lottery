package fr.odyssia.lottery.data;

import fr.odyssia.lottery.Main;
import fr.odyssia.lottery.util.Constants;
import fr.odyssia.lottery.util.Fragment;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class YamlRequest {

    private Main main;

    public YamlRequest(Main main) {
        this.main = main;
    }

    public void createFile() {
        File folder = new File(Bukkit.getServer().getPluginManager().getPlugin(main.getPluginName()).getDataFolder().getPath());

        File file = new File(folder, File.separator + Constants.YML_FILE_NAME);
        FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void addToken(Player player) throws IOException { }

    public void removeToken(Player player) throws IOException { }

    public void addFragment(Player player, Fragment fragment) throws IOException { }

    public void removeFragment(Player player, Fragment fragment) throws IOException { }
}
