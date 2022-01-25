package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.arena.inventory.PracticeInventoryHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class ArenaListener implements Listener {

    /**
     * Listens when a player interact on a practice inventory view
     *
     * @param e the inventory interact event
     */
    @EventHandler
    public void onInventoryClick(InventoryInteractEvent e) {

        //Checks if the inventory is a practice inventory view
        if (e.getInventory().getHolder() instanceof PracticeInventoryHolder) {
            e.setCancelled(true);
        }
    }

}
