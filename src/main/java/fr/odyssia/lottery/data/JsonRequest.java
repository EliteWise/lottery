package fr.odyssia.lottery.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.odyssia.lottery.Main;
import fr.odyssia.lottery.model.LotteryPlayer;
import fr.odyssia.lottery.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonRequest {

    private Main main;

    public JsonRequest(Main main) {
        this.main = main;
    }

    private String mainPath = Bukkit.getServer().getPluginManager().getPlugin("Lottery").getDataFolder().getPath() + "/players/";

    // create object mapper instance
    private static final ObjectMapper mapper = new ObjectMapper();

    public void createPlayersFolder() {
        boolean success = new File(mainPath).mkdirs();

        if(!success) {
            System.out.println("Folder 'players' already initialized!");
        }
    }

    public void createFileAccount(Player player) throws IOException {
        File file = new File(mainPath, player.getUniqueId() + ".json");

        if(!file.exists()) {
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.save(file);

            System.out.println("New Lottery Account: " + mainPath + player.getUniqueId() + ".json");

            // First creation of the file, we have to initialize all fields (with default values) //

            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);

            Map<String, Object> fragmentsMap = ymlConfiguration.getFragments();
            fragmentsMap.entrySet().forEach(entry -> {
                entry.setValue(0);
            });

            // Our Model
            LotteryPlayer lotteryPlayer = new LotteryPlayer(player.getName(), 0, fragmentsMap);
            mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), lotteryPlayer);
        }
    }

    public void addToken(Player player, int tokens) throws IOException {
        JsonNode root = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));
        // Update value
        ((ObjectNode) root).put(Constants.JSON_TOKEN_FIELD, root.get(Constants.JSON_TOKEN_FIELD).intValue() + tokens);
        // Write new value into json file
        mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), root);
    }

    public void removeToken(Player player, int tokens) throws IOException {
        JsonNode root = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));
        // Update value
        ((ObjectNode) root).put(Constants.JSON_TOKEN_FIELD, root.get(Constants.JSON_TOKEN_FIELD).intValue() - tokens);
        // Write new value into json file
        mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), root);
    }

    public int getTokens(Player player) throws IOException {
        JsonNode jsonNode = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));
        return jsonNode.get(Constants.JSON_TOKEN_FIELD).intValue();
    }

    public int getFragments(Player player, String item) throws IOException {
        JsonNode jsonNode = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));
        return jsonNode.get(Constants.JSON_FRAGMENT_FIELD).findValue(item).intValue();
    }

    public void addFragment(Player player, String fragment) throws IOException {
        JsonNode root = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));
        // Update value
        ObjectNode fragmentNode = (ObjectNode) root.path(Constants.JSON_FRAGMENT_FIELD);
        fragmentNode.put(fragment, fragmentNode.path(fragment).intValue() + 1);

        // Write new value into json file
        mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), root);
    }

    public void removeFragment(Player player, String fragment) throws IOException {
        JsonNode root = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));
        // Update value
        ObjectNode fragmentNode = (ObjectNode) root.path(Constants.JSON_FRAGMENT_FIELD);

        YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
        fragmentNode.put(fragment, fragmentNode.path(fragment).intValue() - ymlConfiguration.getFragment(fragment));

        // Write new value into json file
        mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), root);
    }

}
