package fr.bobinho.luxepractice.listeners;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class CooldownListener implements Listener {

    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().setCooldown(Material.ENDER_PEARL, 200);
    }
}
