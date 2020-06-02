package fr.odyssia.lottery;

import fr.odyssia.lottery.data.JsonRequest;
import fr.odyssia.lottery.data.YmlConfiguration;
import fr.odyssia.lottery.util.InventoryFill;
import org.bukkit.*;
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


public class Animation extends BukkitRunnable {

    private fr.odyssia.lottery.Main main;

    private Player player;
    private Inventory inventory;
    private int animationDuration;
    private ItemStack rewardFragment;
    private Item droppedReward;

    public Animation(Main main, Player player, Inventory inventory, int animationDuration) {
        this.main = main;
        this.player = player;
        this.inventory = inventory;
        this.animationDuration = animationDuration;
    }

    int count = 0;
    float soundPitch = 0f;

    @Override
    public void run() {

        if (count == animationDuration) {
            cancel();

            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);

            // Fill border of the inventory at the end of the Animation //

            InventoryFill inventoryFill = new InventoryFill(inventory);
            inventoryFill.fillSidesWithItem(new ItemStack(Material.YELLOW_STAINED_GLASS_PANE));

            JsonRequest jsonRequest = new JsonRequest(main);
            try {
                jsonRequest.addFragment(player, rewardFragment.getType().name());
            } catch (IOException e) {
                e.printStackTrace();
            }

            player.sendMessage(ymlConfiguration.getFragmentMessage(rewardFragment.getType().name()));

            // Spawn floating item on the EnderChest, depending on the position of the villager //

            List<Entity> nearEntities = player.getWorld().getEntities();
            for (Entity entities : nearEntities) {
                if (entities instanceof Villager && entities.getLocation().distance(player.getLocation()) <= 4) {

                    BlockFace eFacing = entities.getFacing();
                    Location eLoc = entities.getLocation();
                    World eWorld = entities.getWorld();

                    double x = eLoc.getX();
                    double y = eLoc.getY() + 2;
                    double z = eLoc.getZ();

                    droppedReward = player.getWorld().dropItemNaturally(new Location(entities.getWorld(), Math.floor(x), y, Math.floor(z), eLoc.getYaw(), eLoc.getPitch()).add(eFacing == BlockFace.NORTH || eFacing == BlockFace.SOUTH ? 2.1 : 0, 1, eFacing == BlockFace.WEST || eFacing == BlockFace.EAST ? 2.1 : 0), rewardFragment);
                    droppedReward.setVelocity(new Vector());
                    droppedReward.setPickupDelay(Integer.MAX_VALUE);

                    // Close inventory 1 second later to let the fill border animation during 1 second //

                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                        @Override
                        public void run() {
                            player.closeInventory();
                            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, ymlConfiguration.enableSounds() ? 0.6f : 0.0f, 0.3f);
                            eWorld.spawnParticle(Particle.valueOf(ymlConfiguration.getParticlesType()), eLoc, ymlConfiguration.getParticlesNumber(), 0,0,0, ymlConfiguration.getParticlesSpeed());
                        }
                    }, 20 * 1);

                    // Used to clear floating item 10 seconds later //

                    Bukkit.getScheduler().runTaskLater(main, new Runnable() {
                        @Override
                        public void run() {
                            entities.getNearbyEntities(2, 2, 2).stream().filter(entstream -> entstream instanceof Item).map(Item.class::cast).filter(item -> item.getItemStack().getType() == droppedReward.getItemStack().getType()).forEach(Item::remove);
                        }
                    }, 20 * 10);
                }
            }
        } else {
            YmlConfiguration ymlConfiguration = new YmlConfiguration(main);

            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, ymlConfiguration.enableSounds() ? 0.6f : 0.0f, soundPitch += 0.1); // Increase sound pitch depending of the animation progress //

            Random random = new Random();
            Set<String> itemsList = ymlConfiguration.getItems();
            int itemrandom = random.nextInt(itemsList.size());
            List<String> list = new ArrayList<>(itemsList);

            rewardFragment = new ItemStack(Material.getMaterial(list.get(itemrandom)));
            for(int i = 1; i < 4; i++) { // Display 3 same items in the center of the inventory
                inventory.setItem(20 + i, rewardFragment);
            }
        }
        count++;
    }
}
