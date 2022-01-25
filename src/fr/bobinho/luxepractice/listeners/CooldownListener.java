package fr.bobinho.luxepractice.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CooldownListener implements Listener {

    /**
     * Listen when a player join the server to set item's cooldown
     *
     * @param e the player join event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        //Sets item's cooldown
        e.getPlayer().setCooldown(Material.ENDER_PEARL, 200);
    }

}
