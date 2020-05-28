package fr.odyssia.lottery.listener;

import fr.odyssia.lottery.Main;
import fr.odyssia.lottery.Animation;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;

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
                    e.getInventory().remove(Material.SUNFLOWER);

                    Animation mainSystem = new Animation(main, player, inventory, ymlConfiguration.getAnimationDuration());
                    mainSystem.runTaskTimer(main, 10, ymlConfiguration.getAnimationSpeed());
                } else {
                    player.closeInventory();
                    player.sendMessage("§cYou don't have enough tokens to play.");
                }
            }
            e.setCancelled(true);
        } else if(e.getView().getTitle().equalsIgnoreCase(Constants.FRAGMENT_INVENTORY_NAME)) {
            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
            JsonRequest jsonRequest = new JsonRequest(main);

            String itemName = item.getItemMeta().getDisplayName().replace("Fragment ", "");

            for(String materialName : ymlConfiguration.getItems()) {
                if(itemName.equals(materialName)) {
                    System.out.println(jsonRequest.getFragments(player, itemName));
                    if((jsonRequest.getFragments(player, itemName) != 0) && (jsonRequest.getFragments(player, itemName) % ymlConfiguration.getFragments(itemName) == 0)) {
                        item.setAmount(1);
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
            e.setCancelled(true);
            org.bukkit.inventory.Inventory fragmentInventory = Bukkit.createInventory(null, 54, Constants.FRAGMENT_INVENTORY_NAME);
            player.openInventory(fragmentInventory);

            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
            JsonRequest jsonRequest = new JsonRequest(main);
            int index = 0;
            for(String material : ymlConfiguration.getItems()) {
                ItemStack item = new ItemStack(jsonRequest.getFragments(player, material) == 0 ? Material.BARRIER : Material.getMaterial(material),
                        jsonRequest.getFragments(player, material) == 0 ? 1 : jsonRequest.getFragments(player, material));
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setDisplayName("Fragment " + material);
                item.setItemMeta(itemMeta);
                fragmentInventory.setItem(index++, item);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException {
        Player player = e.getPlayer();
        JsonRequest jsonRequest = new JsonRequest(main);
        jsonRequest.createFileAccount(player);
        jsonRequest.addFragment(player, "BLAZE_POWDER");
    }

    // Simple Example to add a Token //

    @EventHandler
    public void onWriteTokenInChat(AsyncPlayerChatEvent e) throws IOException {
        if (e.getMessage().contains("POWDER")) {
            JsonRequest jsonRequest = new JsonRequest(main);

        }
    }

}
