package fr.odyssia.lottery.listener;

import fr.odyssia.lottery.util.Constants;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.EnderChest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class Inventory implements Listener {

    // PNJ INTERACTION //

    @EventHandler
    public void onClickOnVillager(PlayerInteractEntityEvent e) {
        Entity entity = e.getRightClicked();
        if (entity.getType() == EntityType.VILLAGER && entity.getCustomName() == Constants.LOTTERY_VILLAGER_NAME) {
            e.setCancelled(true);
        }
    }

    // LOTTERY INVENTORY //

    @EventHandler
    public void onTryToCloseLotteryInventory(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if(e.getView().getTitle().equalsIgnoreCase(Constants.LOTTERY_INVENTORY_NAME)) {
            player.openInventory(e.getView());
        }
    }

    // FRAGMENT INVENTORY //

    @EventHandler
    public void onClickOnEnderChest(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();

        if(block instanceof EnderChest) {
            org.bukkit.inventory.Inventory fragmentInventory = Bukkit.createInventory(null, 45, Constants.FRAGMENT_INVENTORY_NAME);
            player.openInventory(fragmentInventory);
        }
    }

}

