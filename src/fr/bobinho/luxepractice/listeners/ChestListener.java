package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.chest.PracticeChestManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ChestListener implements Listener {

    /**
     * Listen when a player open a practice chest
     *
     * @param e the player interact event
     */
    @EventHandler
    public void onOpenChest(PlayerInteractEvent e) {
        PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId()).ifPresent(practicePlayer -> {

            //Checks if the clicked block is a chest
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.CHEST) {

                //Checks if the clicked chest is a practice chest
                if (PracticeChestManager.isItPracticeChest(e.getClickedBlock().getLocation())) {

                    //Opens the practice chest
                    PracticeChestManager.openPracticeChest(practicePlayer, (Chest) e.getClickedBlock().getState());
                    e.setCancelled(true);
                }
            }
        });
    }

    /**
     * Listen when a practice chest is broken
     *
     * @param e the block break event
     */
    @EventHandler
    public void onOpenChest(BlockBreakEvent e) {

        //Checks if the event block is a chest
        if (e.getBlock().getType() == Material.CHEST) {

            //Checks if the event chest is a practice chest
            if (PracticeChestManager.isItPracticeChest(e.getBlock().getLocation())) {

                //Deletes the practice chest
                PracticeChestManager.deletePracticeChest(e.getBlock().getLocation());
            }
        }
    }

}