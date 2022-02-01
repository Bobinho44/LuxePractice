package fr.bobinho.luxepractice.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class CooldownListener implements Listener {

    /*@EventHandler
    public void onInteract(PlayerInteractEvent e) {

        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK && e.getItem() != null && e.getItem().getType() == Material.ENDER_PEARL) {
           if (e.getPlayer().getCooldownPeriod() != 0) return;

            e.getPlayer().setCooldown(Material.ENDER_PEARL, 200);
        }
    }*/

}
