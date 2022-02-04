package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.LuxePracticeCore;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class CooldownListener implements Listener {

    @EventHandler
    public void onUseEnderPearl(PlayerTeleportEvent e) {

        //Checks if a practice player use an ender pearl
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            e.getPlayer().setCooldown(Material.ENDER_PEARL, 20 * LuxePracticeCore.getMainSettings().getConfiguration().getInt("ENDER_PEARL"));
        }
    }

}
