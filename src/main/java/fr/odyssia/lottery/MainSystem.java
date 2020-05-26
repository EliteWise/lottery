package fr.odyssia.lottery;

import fr.odyssia.lottery.data.JsonRequest;
import fr.odyssia.lottery.data.YmlConfiguration;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;


public class MainSystem extends BukkitRunnable {

    private fr.odyssia.lottery.Main main;
    private YmlConfiguration ymlConfiguration = new YmlConfiguration(main);

    private Player player;
    private Inventory inventory;
    private ItemStack rewardFragment;

    public MainSystem(Main main, Player player, Inventory inventory) {
        this.main = main;
        this.player = player;
        this.inventory = inventory;
    }

    int count = 0;

    @Override
    public void run() {

        if (count == ymlConfiguration.getAnimationDuration()) {
            cancel();

            player.getInventory().addItem(rewardFragment);
            player.closeInventory();

            JsonRequest jsonRequest = new JsonRequest(main);
            try {
                jsonRequest.addFragment(player, rewardFragment.getType().name());
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Entity> nearEntities = player.getWorld().getEntities();
            for (Entity entities : nearEntities) {
                if (entities instanceof Villager && entities.getLocation().distance(player.getLocation()) <= 4) {
                    player.getWorld().dropItem(entities.getLocation().add(2, 1, 0), rewardFragment);
                }
            }
        } else {
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
