package fr.odyssia.lottery.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.odyssia.lottery.model.LotteryPlayer;
import fr.odyssia.lottery.Main;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class JsonRequest {

    private Main main;
    private String mainPath = Bukkit.getServer().getPluginManager().getPlugin("Lottery").getDataFolder().getPath() + "/players/";

    // create object mapper instance
    private static final ObjectMapper mapper = new ObjectMapper();

    public JsonRequest(Main main) {
        this.main = main;
    }

    public void createPlayersFolder() {
        boolean success = new File(mainPath).mkdirs();

        if(!success) {
            System.out.println("An Error has occurred!");
        }
    }

    public void createFileAccount(Player player) throws IOException {
        File file = new File(mainPath, player.getUniqueId() + ".json");

        if(!file.exists()) {
            FileConfiguration fileConfiguration = YamlConfiguration.loadConfiguration(file);
            fileConfiguration.save(file);

            // First creation of the file, we have to initialize all fields (with default values) //

            // Our Model
            LotteryPlayer lotteryPlayer = new LotteryPlayer(player.getName(), 0);
            mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), lotteryPlayer);
        }
    }

    public void addToken(Player player, int tokens) throws IOException {
        JsonNode root = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));

        // Update value
        ((ObjectNode) root).put("tokens", root.get("tokens").intValue() + tokens);

        // Write new value into json file
        mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), root);
    }

    public void removeToken(Player player, int tokens) throws IOException {
        JsonNode root = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));

        // Update value
        ((ObjectNode) root).put("tokens", root.get("tokens").intValue() - tokens);

        // Write new value into json file
        mapper.writeValue(new File(mainPath + player.getUniqueId() + ".json"), root);
    }

    public int getTokens(Player player) throws IOException {
        JsonNode jsonNode = mapper.readTree(new File(mainPath + player.getUniqueId() + ".json"));
        JsonNode tokens = jsonNode.get("tokens");
        return tokens.intValue();
    }

    public void addFragment(Player player, String fragment) throws IOException { }

    public void removeFragment(Player player, String fragment) throws IOException { }

}
