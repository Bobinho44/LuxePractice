package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.inventory.PracticeInventoryHolder;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class ArenaListener implements Listener {

    /**
     * Listens when a practice player interact on a practice inventory view
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
     * Listens when a practice spectator interact with his inventory
     *
     * @param e the inventory click event
     */
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            PracticePlayerManager.getPracticePlayer(e.getWhoClicked().getUniqueId()).ifPresent(practicePlayer -> {

                //Checks if the practice player is a spectator in a practice match
                if (PracticeMatchManager.isInMatch(practicePlayer) && PracticeMatchManager.getPracticeMatch(practicePlayer).get().isItSpectator(practicePlayer)) {
                    e.setCancelled(true);

                    //Views the practice fighter inventory
                    if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {

                        PracticeArena practiceArena = PracticeMatchManager.getPracticeMatch(practicePlayer).get().getArena();
                        UUID uuid = Bukkit.getOfflinePlayer(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())).getUniqueId();

                        Bukkit.dispatchCommand(practicePlayer.getSpigotPlayer(), "practiceinventory Mm7kTCD2 " + uuid + " " + practiceArena.getName());
                    }
                }
            });
        }
    }

    /**
     * Listens when a practice spectator click on his "back to spawn" item
     *
     * @param e the player interact event
     */
    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId()).ifPresent(practicePlayer -> {

            //Checks if the practice player click on the "back to spawn" item
            if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().getType() == Material.OAK_DOOR) {

                //Removes practice player from the practice match
                if (PracticeMatchManager.isInMatch(practicePlayer) && PracticeMatchManager.getPracticeMatch(practicePlayer).get().isItSpectator(practicePlayer)) {
                    PracticeMatchManager.removeSpectator(practicePlayer);
                    practicePlayer.getSpigotPlayer().closeInventory();
                }
            }
        });
    }

    /**
     * Listens when a practice player is damaged by a practice player during a practice match
     *
     * @param e the entity damage by entity event
     */
    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId()).ifPresent(practiceVictim ->
                    PracticePlayerManager.getPracticePlayer(e.getDamager().getUniqueId()).ifPresent(practiceAttacker -> {

                        //Checks if players are in a match
                        if (PracticeMatchManager.isInMatch(practiceVictim)) {
                            PracticeMatch practiceMatch = PracticeMatchManager.getPracticeMatch(practiceVictim).get();

                            //Checks if the victim or the attacker is a spectator
                            if (practiceMatch.isItSpectator(practiceVictim) || practiceMatch.isItSpectator(practiceAttacker)) {
                                e.setCancelled(true);
                            }
                        }
                    }));
        }
    }

    /**
     * Listens when a practice spectator is damaged during a practice match
     *
     * @param e the entity damage event
     */
    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId()).ifPresent(practiceVictim -> {

                //Checks if players is in a match
                if (PracticeMatchManager.isInMatch(practiceVictim)) {
                    PracticeMatch practiceMatch = PracticeMatchManager.getPracticeMatch(practiceVictim).get();

                    //Checks if the victim is a spectator
                    if (practiceMatch.isItSpectator(practiceVictim)) {
                        e.setCancelled(true);
                    }
                }
            });
        }
    }

}