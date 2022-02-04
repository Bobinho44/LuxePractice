package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.arena.inventory.PracticeInventoryHolder;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;

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
     * Listens when a practice player die during a practice match
     *
     * @param e the player death event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId()).ifPresent(practicePlayer -> {

            //Checks if the player is in a match
            if (PracticeMatchManager.isInMatch(practicePlayer)) {

                //Adds the player to the death list and check if the match is finished
                PracticeMatchManager.addSpectator(practicePlayer, practicePlayer);
            }
        });
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
                        Bukkit.dispatchCommand(practicePlayer.getSpigotPlayer(), "practiceinventory " + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
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
     * Listens when a practice player is damaged during a practice match
     *
     * @param e the player death event
     */
    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
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
     * Listens when a practice player open his spectator inventory
     *
     * @param e the inventory open event
     */
    @EventHandler
    public void onOpenInventory(InventoryOpenEvent e) {
        if (e.getInventory().getType() == InventoryType.PLAYER && !(e.getInventory() instanceof PracticeInventoryHolder)) {
            PracticeKitManager.giveSpectatorPracticeKit(PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId()).get());
        }
    }

}