package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.chest.PracticeChestManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {

    public void onOpenChest(PlayerInteractEvent e) {
        Block clickedBlock = e.getClickedBlock();
        Player player = e.getPlayer();

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && clickedBlock != null && clickedBlock.getType() == Material.CHEST) {
            if (PracticeChestManager.isItPracticeChest(clickedBlock.getLocation())) {
                PracticeChestManager.openPracticeChest(player, (Chest) clickedBlock);
                e.setCancelled(true);
            }
        }
    }
}
