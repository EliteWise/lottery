package fr.odyssia.lottery;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class Inventory implements Listener {

    @EventHandler
    public void onClicOnVillager(PlayerInteractEntityEvent e) {

        if (e.getRightClicked().getType()== EntityType.VILLAGER && e.getRightClicked().getCustomName()== "Lotterie"){
        e.setCancelled(true);


        }

    }

}

