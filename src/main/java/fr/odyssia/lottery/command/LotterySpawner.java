package fr.odyssia.lottery.command;

import fr.odyssia.lottery.Main;
import fr.odyssia.lottery.data.YmlConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;


public class LotterySpawner implements CommandExecutor {

    public Main main;

    public LotterySpawner(Main main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {

            Player p = (Player) sender;
            Location loc = p.getLocation();
            Location enderChestLoc = p.getLocation().add(2, 0, 0);

            if (enderChestLoc.getBlock().getType() == Material.AIR) {

                YmlConfiguration ymlConfiguration = new YmlConfiguration(main);

                Villager villager = (Villager) p.getWorld().spawnEntity(loc, EntityType.VILLAGER);
                villager.setProfession(Villager.Profession.valueOf(ymlConfiguration.getProfession()));
                villager.setAdult();
                villager.setRotation(loc.getYaw(), loc.getPitch());
                villager.setInvulnerable(true);
                villager.setAI(false);
                villager.setCustomName(ymlConfiguration.getNpcName());
                villager.setCustomNameVisible(true);

                enderChestLoc.getBlock().setType(Material.ENDER_CHEST);

                Block enderChest = enderChestLoc.getBlock();
                Directional data = (Directional) enderChest.getBlockData();
                data.setFacing(p.getFacing());
                enderChest.setBlockData(data);

            } else {
                p.sendMessage("§cThere are less than 3 AIR blocks around you, you can't place Lottery here.");
            }
        }

        return false;
    }
}