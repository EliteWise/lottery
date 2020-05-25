package fr.odyssia.lottery;

import fr.odyssia.lottery.data.YmlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;


public class MainSystem  extends BukkitRunnable {
    private fr.odyssia.lottery.Main main;
    private YmlConfiguration ymlConfiguration = new YmlConfiguration(main);
    private Inventory inventory;

    public MainSystem(Main main, Inventory inventory){
        this.inventory = inventory;
        this.main = main;


    }


    public void RandomItem(Inventory inventory, Player player){
        Random random = new Random();

        Set<String> itemsList = ymlConfiguration.getItems();
        int itemrandom = random.nextInt(itemsList.size());

        YmlConfiguration ymlConfiguration = new YmlConfiguration(main);




    }

    int i = 0;
    @Override
    public void run() {

        if (i == ymlConfiguration.getAnimationDuration()) {
            cancel();

        }else {
            Random random = new Random();
            Set<String> itemsList = ymlConfiguration.getItems();
            int itemrandom = random.nextInt(itemsList.size());
            List<String> list = new ArrayList<>(itemsList);
            inventory.setItem(21, new ItemStack(Material.getMaterial(list.get(itemrandom))));
            inventory.setItem(22, new ItemStack(Material.getMaterial(list.get(itemrandom))));
            inventory.setItem(23, new ItemStack(Material.getMaterial(list.get(itemrandom))));
        }
    }
}
