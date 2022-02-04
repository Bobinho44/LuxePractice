package fr.bobinho.luxepractice.utils.arena.inventory;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import javax.annotation.Nonnull;

public class PracticeInventoryHolder implements InventoryHolder {

    /**
     * {@inheritDoc}
     */
    @Nonnull
    @Override
    public Inventory getInventory() {
        return Bukkit.createInventory(null, InventoryType.PLAYER);
    }

}
