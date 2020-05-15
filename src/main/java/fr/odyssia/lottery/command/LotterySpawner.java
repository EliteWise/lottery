package fr.odyssia.lottery.command;

import fr.odyssia.lottery.util.Constants;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;

public class LotterySpawner implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Location loc = p.getLocation();
            Villager villager = (Villager) p.getWorld().spawnEntity(loc , EntityType.VILLAGER);
            villager.setRotation(loc.getYaw(), loc.getPitch());
            villager.setInvulnerable(true);
            villager.setCustomName(ChatColor.YELLOW + Constants.LOTTERY_VILLAGER_NAME);
            villager.setCustomNameVisible(true);
        }
            return false;
        }


}
