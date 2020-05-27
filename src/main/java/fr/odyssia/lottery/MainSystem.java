package fr.odyssia.lottery;

import fr.odyssia.lottery.data.JsonRequest;
import fr.odyssia.lottery.data.YmlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class MainSystem extends BukkitRunnable {

    private fr.odyssia.lottery.Main main;

    private Player player;
    private Inventory inventory;
    private int animationDuration;
    private ItemStack rewardFragment;
    private Item droppedReward;

    public MainSystem(Main main, Player player, Inventory inventory, int animationDuration) {
        this.main = main;
        this.player = player;
        this.inventory = inventory;
        this.animationDuration = animationDuration;
    }

    int count = 0;
    float soundLevel = 0f;

    @Override
    public void run() {

        if (count == animationDuration) {
            cancel();

            player.closeInventory();
            player.getWorld().playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 0.6f, 0.3f);

            JsonRequest jsonRequest = new JsonRequest(main);
            try {
                jsonRequest.addFragment(player, rewardFragment.getType().name());
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Entity> nearEntities = player.getWorld().getEntities();
            for (Entity entities : nearEntities) {
                if (entities instanceof Villager && entities.getLocation().distance(player.getLocation()) <= 4) {

                    BlockFace eFacing = entities.getFacing();
                    Location eLoc = entities.getLocation();
                    double x = eLoc.getX();
                    double y = eLoc.getY() + 2;
                    double z = eLoc.getZ();
                    droppedReward = player.getWorld().dropItemNaturally(new Location(entities.getWorld(), Math.floor(x), y, Math.floor(z), eLoc.getYaw(), eLoc.getPitch()).add(eFacing == BlockFace.NORTH || eFacing == BlockFace.SOUTH ? 2.1 : 0, 1, eFacing == BlockFace.WEST || eFacing == BlockFace.EAST ? 2.1 : 0), rewardFragment);
                    droppedReward.setVelocity(new Vector());
                    droppedReward.setPickupDelay(Integer.MAX_VALUE);

                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                        @Override
                        public void run() {
                            entities.getNearbyEntities(2, 2, 2).stream().filter(entstream -> entstream instanceof Item).map(Item.class::cast).filter(item -> item.getItemStack().getType() == droppedReward.getItemStack().getType()).forEach(Item::remove);
                        }
                    }, 20 * 10);
                }
            }
        } else {
            player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 0.6f, soundLevel += 0.1);

            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
            Random random = new Random();
            Set<String> itemsList = ymlConfiguration.getItems();
            int itemrandom = random.nextInt(itemsList.size());
            List<String> list = new ArrayList<>(itemsList);

            rewardFragment = new ItemStack(Material.getMaterial(list.get(itemrandom)));
            inventory.setItem(21, rewardFragment);
            inventory.setItem(22, rewardFragment);
            inventory.setItem(23, rewardFragment);
        }
        count++;
    }
}
