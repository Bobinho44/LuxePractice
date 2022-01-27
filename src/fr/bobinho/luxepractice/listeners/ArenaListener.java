package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.arena.inventory.PracticeInventoryHolder;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
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

    /**
     * Listens when a player die in a practice match
     *
     * @param e the player death event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId());

        //Checks if the player is in a match
        if (PracticeMatchManager.isInMatch(practicePlayer)) {

            //Adds the player to the death list and check if the match is finished
            PracticeMatch practiceMatch = PracticeMatchManager.getMatch(practicePlayer).get();
            practiceMatch.addDeathPracticePlayers(practicePlayer);
        }
    }

    /**
     * Listens when a player die in a practice match
     *
     * @param e the player death event
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            PracticePlayer practiceVictim = PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId());
            PracticePlayer practiceAttacker = PracticePlayerManager.getPracticePlayer(e.getDamager().getUniqueId());

            //Checks if players are in a match
            if (PracticeMatchManager.isInMatch(practiceVictim)) {
                PracticeMatch practiceMatch = PracticeMatchManager.getMatch(practiceVictim).get();

                //Checks if the victim or the attacker is a spectator
                if (practiceMatch.isSpectator(practiceVictim) || practiceMatch.isSpectator(practiceAttacker)) {
                    e.setCancelled(true);
                }
            }
        }
    }

}
