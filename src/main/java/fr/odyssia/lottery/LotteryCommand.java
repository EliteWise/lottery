package fr.odyssia.lottery;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;

public class LotteryCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location loc = p.getLocation();
            Villager villager = (Villager) p.getWorld().spawnEntity(loc , EntityType.VILLAGER);
            villager.setRotation(loc.getYaw(), loc.getPitch());
            villager.setInvulnerable(true);
            villager.setCustomName(ChatColor.YELLOW+"Lotterie");
            villager.setCustomNameVisible(true);

        }
            return false;
        }


}
