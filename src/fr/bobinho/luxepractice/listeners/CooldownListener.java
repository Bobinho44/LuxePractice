package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

public class CooldownListener implements Listener {

    /**
     * Listen when a practice player use an ender pearl
     *
     * @param e the player interact event
     */
    @EventHandler
    public void onUseEnderPearl(PlayerInteractEvent e) {
        if (e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {

            //Checks if the practice player is already in the cooldown
            if (e.getPlayer().hasCooldown(Material.ENDER_PEARL)) {
                return;
            }

            //Adds cooldown
            PracticeScheduler.syncScheduler().after(50, TimeUnit.MILLISECONDS).run(() ->
                    e.getPlayer().setCooldown(Material.ENDER_PEARL, 20 * LuxePracticeCore.getMainSettings().getConfiguration().getInt("cooldown.ENDER_PEARL")));
        }
    }

}