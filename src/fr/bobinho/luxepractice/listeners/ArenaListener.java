package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.arena.inventory.PracticeInventoryHolder;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeWaitList;
import fr.bobinho.luxepractice.utils.chest.PracticeChestManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
        PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId());


        //Checks if the player is in a match
        if (PracticeMatchManager.isInMatch(practicePlayer)) {

            //Adds the player to the death list and check if the match is finished
            PracticeMatch practiceMatch = PracticeMatchManager.getMatch(practicePlayer).get();
            practiceMatch.addDeathPracticePlayers(practicePlayer);
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            Player player = (Player) e.getWhoClicked();
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            if (PracticeMatchManager.isInMatch(practicePlayer) && PracticeMatchManager.getMatch(practicePlayer).get().isSpectator(practicePlayer)) {
                e.setCancelled(true);
                if (e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                    Bukkit.dispatchCommand(player, "practiceinventory " + ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                }
            }

        }
    }

    @EventHandler
    public void onclick(PlayerInteractEvent e) {
        Player player = (Player) e.getPlayer();
        PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getItem() != null && e.getItem().getType() == Material.OAK_DOOR) {
            if (PracticeMatchManager.isInMatch(practicePlayer) && PracticeMatchManager.getMatch(practicePlayer).get().isSpectator(practicePlayer)) {
                practicePlayer.teleportToTheSpawn();
                PracticeMatchManager.removeSpectator(practicePlayer);
                player.closeInventory();
            }
        }
    }

    /**
     * Listens when a practice player is damaged during a practice match
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
