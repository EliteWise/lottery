package fr.odyssia.lottery.listener;

import fr.odyssia.lottery.Main;
import fr.odyssia.lottery.data.JsonRequest;
import fr.odyssia.lottery.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
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

import java.io.IOException;

public class Inventory implements Listener {

    private Main main;
    private JsonRequest jsonRequest = new JsonRequest(this.main);


    public Inventory(Main main) {
        this.main = main;
    }

    // PNJ INTERACTION //

    @EventHandler
    public void onClickOnVillager(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();

        if (entity.getType() == EntityType.VILLAGER && entity.getCustomName().contains(Constants.LOTTERY_VILLAGER_NAME)) {
            e.setCancelled(true);
            org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 45, Constants.LOTTERY_VILLAGER_NAME);
            ItemStack apple = new ItemStack(Material.SUNFLOWER); //exemple
            inv.setItem(22, apple);


            player.openInventory(inv);
        }

    }

    // LOTTERY INVENTORY //
    @EventHandler
    public void onClickInInventory(InventoryClickEvent e) throws IOException {
        Player player = (Player) e.getWhoClicked();
        ItemStack item =  e.getCurrentItem();

        if (item.getType() == Material.SUNFLOWER){
            if (jsonRequest.getTokens(player)>0){
                jsonRequest.removeToken(player, main.getConfig().getInt("payment-token", 1));
                e.getInventory().remove(Material.SUNFLOWER);

            }


        }

    }


    // FRAGMENT INVENTORY //

    @EventHandler
    public void onClickOnEnderChest(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ENDER_CHEST) {
            e.setCancelled(true);
            org.bukkit.inventory.Inventory fragmentInventory = Bukkit.createInventory(null, 54, Constants.FRAGMENT_INVENTORY_NAME);
            player.openInventory(fragmentInventory);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) throws IOException {
        Player player = e.getPlayer();
        jsonRequest.createFileAccount(player);
    }

    // Simple Example to add a Token //

    @EventHandler
    public void onWriteTokenInChat(AsyncPlayerChatEvent e) throws IOException {
        if (e.getMessage().contains("token")) {
            jsonRequest.addToken(e.getPlayer(), 1);
        }
    }

}
