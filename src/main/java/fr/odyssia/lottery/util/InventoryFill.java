package fr.odyssia.lottery.util;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryFill {

    private final org.bukkit.inventory.Inventory inv;
    private List<Integer> sideSlots = new ArrayList<>();

    public InventoryFill(Inventory inv) {
        this.inv = inv;
    }

    public void fillSidesWithItem(ItemStack item) {
        int size = inv.getSize();
        int rows = size / 9;

        if(rows >= 3) {
            for (int i = 0; i <= 8; i++) {
                this.inv.setItem(i, item);

                sideSlots.add(i);
            }

            for(int s = 8; s < (this.inv.getSize() - 9); s += 9) {
                int lastSlot = s + 1;
                this.inv.setItem(s, item);
                this.inv.setItem(lastSlot, item);

                sideSlots.add(s);
                sideSlots.add(lastSlot);
            }

            for (int lr = (this.inv.getSize() - 9); lr < this.inv.getSize(); lr++) {
                this.inv.setItem(lr, item);

                sideSlots.add(lr);
            }
        }
    }

    public List<Integer> getNonSideSlots() {
        List<Integer> availableSlots = new ArrayList<>();

        for (int i = 0; i < this.inv.getSize(); i++) {
            if(!this.sideSlots.contains(i)) {
                availableSlots.add(i);
            }
        }

        return availableSlots;
    }
}
