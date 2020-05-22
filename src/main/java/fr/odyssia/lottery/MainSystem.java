package fr.odyssia.lottery;

import fr.odyssia.lottery.data.YmlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Random;
import java.util.Set;


public class MainSystem {
    private fr.odyssia.lottery.Main main;
    private YmlConfiguration ymlConfiguration = new YmlConfiguration(main);

    public MainSystem(Main main ){
        this.main = main;

    }
    public void RandomItem(Inventory inventory, Player player){
        Random random = new Random();

        Set<String> itemsList = ymlConfiguration.getItems();
        int itemrandom = random.nextInt(itemsList.size());



    }
}
