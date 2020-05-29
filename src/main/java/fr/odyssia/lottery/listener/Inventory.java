package fr.odyssia.lottery.listener;

import fr.odyssia.lottery.Animation;
import fr.odyssia.lottery.Main;
import fr.odyssia.lottery.data.JsonRequest;
import fr.odyssia.lottery.data.YmlConfiguration;
import fr.odyssia.lottery.util.Constants;
import fr.odyssia.lottery.util.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Inventory implements Listener {

    private Main main;

    public Inventory(Main main) {
        this.main = main;
    }

    // PNJ INTERACTION //

    @EventHandler
    public void onClickOnVillager(PlayerInteractEntityEvent e) throws IOException {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();

        YmlConfiguration ymlConfiguration = new YmlConfiguration(main);

        if (entity.getType() == EntityType.VILLAGER && entity.getCustomName().equalsIgnoreCase(ymlConfiguration.getNpcName())) {
            e.setCancelled(true);

            JsonRequest jsonRequest = new JsonRequest(main);

            // Make sure player account exist to avoid errors //
            jsonRequest.createFileAccount(player);

            org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 45, Constants.LOTTERY_INVENTORY_NAME);

            ItemCreator itemCreator = new ItemCreator();
            inv.setItem(22, itemCreator.create(Material.getMaterial(ymlConfiguration.getTokenType()), ("§eTokens: §f§l" + getTokens(player, ymlConfiguration)), Enchantment.LUCK));

            player.openInventory(inv);
        }

    }

    // LOTTERY INVENTORY //

    public void tokenCheck(Player player, YmlConfiguration ymlConfiguration, JsonRequest jsonRequest) throws IOException {
        for(ItemStack tokenItem : player.getInventory().getContents()) {
            if(tokenItem == null) {
                continue;
            } else if(tokenItem.getType() == Material.getMaterial(ymlConfiguration.getTokenType())) {
                tokenItem.setAmount(tokenItem.getAmount() - 1);
                jsonRequest.addToken(player, 1);
                return;
            }
        }
    }

    // This method is used to get in-game item tokens specified is config.yml, it's the common usage of tokens //

    public int getTokens(Player player, YmlConfiguration ymlConfiguration) {
        int amount = 0;
        for(ItemStack tokenItem : player.getInventory().getContents()) {
            if(tokenItem == null) {
                continue;
            } else if(tokenItem.getType() == Material.getMaterial(ymlConfiguration.getTokenType())) {
                amount += tokenItem.getAmount();
            }
        }
        return amount;
    }

    @EventHandler
    public void onClickInInventory(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        org.bukkit.inventory.Inventory inventory = e.getInventory();
        if(item == null) return;

        if(e.getView().getTitle().equalsIgnoreCase(Constants.LOTTERY_INVENTORY_NAME)) {

            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
            JsonRequest jsonRequest = new JsonRequest(main);

            if (item.getType() == Material.getMaterial(ymlConfiguration.getTokenType())){

                tokenCheck(player, ymlConfiguration, jsonRequest);

                if (jsonRequest.getTokens(player) > 0){
                    jsonRequest.removeToken(player, main.getConfig().getInt("payment-token", 1));
                    e.getInventory().remove(Material.getMaterial(ymlConfiguration.getTokenType()));

                    // Beginning of the Animation //

                    Animation mainSystem = new Animation(main, player, inventory, ymlConfiguration.getAnimationDuration());
                    mainSystem.runTaskTimer(main, 10, ymlConfiguration.getAnimationSpeed());
                } else {
                    player.closeInventory();
                    player.sendMessage("§cYou don't have enough tokens to play.");
                }
            }
            e.setCancelled(true);
        } else if(e.getView().getTitle().equalsIgnoreCase(Constants.FRAGMENT_INVENTORY_NAME)) {

            // Barrier is only used when a player don't have fragments //

            if(item.getType() == Material.BARRIER) {
                e.setCancelled(true);
                return;
            }

            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
            JsonRequest jsonRequest = new JsonRequest(main);

            String itemName = item.getType().name();
            int playerFragments = jsonRequest.getFragments(player, itemName);
            int limitFragments = ymlConfiguration.getFragment(itemName);

            for(String materialName : ymlConfiguration.getItems()) {
                if(itemName.equals(materialName)) {
                    if((playerFragments != 0) && (playerFragments % limitFragments == 0) || (playerFragments > limitFragments)) { // Player can reach the fragments limit
                        item.setAmount(1);

                        ItemMeta itemMeta = item.getItemMeta();
                        itemMeta.setLore(null);
                        item.setItemMeta(itemMeta);

                        player.getInventory().addItem(item);
                        player.closeInventory();
                        player.sendMessage("§aCongratulations ! §e§l" + itemName.replace("_", " ") + " §aadded to your inventory.");
                        jsonRequest.removeFragment(player, itemName);
                    }
                }
            }
            e.setCancelled(true);
        }

    }

    // FRAGMENT INVENTORY //

    @EventHandler
    public void onClickOnEnderChest(PlayerInteractEvent e) throws IOException {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ENDER_CHEST) {

            // Check if a villager is near clicked EnderChest, to allow basic usage if not //

            List<Entity> nearEntities = player.getWorld().getEntities();

            for (Entity entities : nearEntities) {
                if (entities instanceof Villager && entities.getLocation().distance(block.getLocation()) <= 3) {
                    e.setCancelled(true);

                    YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
                    JsonRequest jsonRequest = new JsonRequest(main);

                    // Make sure player account exist to avoid errors //
                    jsonRequest.createFileAccount(player);

                    org.bukkit.inventory.Inventory fragmentInventory = Bukkit.createInventory(null, 54, Constants.FRAGMENT_INVENTORY_NAME);
                    player.openInventory(fragmentInventory);

                    int index = 0;

                    for (String material : ymlConfiguration.getItems()) {
                        int playerFragments = jsonRequest.getFragments(player, material);

                        ItemStack item = new ItemStack(playerFragments == 0 ? Material.BARRIER : Material.getMaterial(material),
                                playerFragments == 0 ? 1 : playerFragments);
                        ItemMeta itemMeta = item.getItemMeta();

                        List<String> description = new ArrayList<>();
                        description.add("§eFragments: §f" + playerFragments + "§e/§f" + ymlConfiguration.getFragment(material));
                        description.add("§7Click to receive");

                        itemMeta.setLore(description);
                        item.setItemMeta(itemMeta);
                        fragmentInventory.setItem(index++, item);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException {
        Player player = e.getPlayer();
        JsonRequest jsonRequest = new JsonRequest(main);

        // Make sure player account exist to avoid errors //
        jsonRequest.createFileAccount(player);
    }

}
