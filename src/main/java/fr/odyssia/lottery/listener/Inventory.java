package fr.odyssia.lottery.listener;

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
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Inventory implements Listener {

    // PNJ INTERACTION //

    @EventHandler
    public void onClickOnVillager(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        if (entity.getType() == EntityType.VILLAGER && entity.getCustomName().contains(Constants.LOTTERY_VILLAGER_NAME)) {
            org.bukkit.inventory.Inventory inv = Bukkit.createInventory(null, 45, Constants.LOTTERY_VILLAGER_NAME);
            ItemStack apple = new ItemStack(Material.ACACIA_BUTTON); //exemple
            inv.setItem(22, apple);
            player.openInventory(inv);
        }
    }

    // LOTTERY INVENTORY //

    // FRAGMENT INVENTORY //

    @EventHandler
    public void onClickOnEnderChest(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && block.getType() == Material.ENDER_CHEST) {
            e.setCancelled(true);
            org.bukkit.inventory.Inventory fragmentInventory = Bukkit.createInventory(null, 54, Constants.FRAGMENT_INVENTORY_NAME);
            player.openInventory(fragmentInventory);
        }
    }

}
