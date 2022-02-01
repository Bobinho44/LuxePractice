package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeWaitList;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerListener implements Listener {

    /**
     * Listen when a player join the server
     *
     * @param e the player join event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        //Loads player's data
        PracticePlayerManager.loadPracticePlayerData(e.getPlayer().getUniqueId());

        //Teleport the player to the spawn
        e.getPlayer().teleport(PracticeLocationUtil.getAsLocation(PracticeSettings.getConfiguration().getString("spawn")));
    }

    /**
     * Listen when a player leave the server
     *
     * @param e the player quit event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {

        PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId());

        //Checks if the player is in a match
        if (PracticeMatchManager.isInMatch(practicePlayer)) {
            PracticeMatch practiceMatch = PracticeMatchManager.getMatch(practicePlayer).get();

            //Checks if the player is not spectator
            if (!practiceMatch.isSpectator(practicePlayer)) {

                //Kills the player
                practiceMatch.addDeathPracticePlayers(practicePlayer);
            }
            PracticeMatchManager.removeSpectator(practicePlayer);
        }

        if (PracticeWaitList.isAlreadyInThePracticeWaitList(practicePlayer)) {
            PracticeWaitList.removePracticePlayerToTheWaitList(practicePlayer);
        }

        //Saves player's data
        PracticePlayerManager.savePracticePlayerData(e.getPlayer().getUniqueId());
        PracticePlayerManager.removePlayer(practicePlayer);
    }

    /**
     * Listen when a player respawn
     *
     * @param e the player respawn event
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId());

        if (PracticeMatchManager.isInMatch(practicePlayer)) {
            Bukkit.getConsoleSender().sendMessage("NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
            PracticeKitManager.giveSpectatorPracticeKit(practicePlayer);
            practicePlayer.teleportAroundLocation(PracticeMatchManager.getMatch(practicePlayer).get().getArena().getSpawn());
            practicePlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 1));
            practicePlayer.setAllowFlight(true);
            return;
        }

        //Checks if the player has enabled an auto kit
        if (PracticeKitManager.haveAutoPracticeKit(practicePlayer)) {
            Bukkit.getConsoleSender().sendMessage("FGERHDSJQKLDFJQJDFKH");
            //Gives the auto kit
            PracticeKitManager.givePracticeKit(practicePlayer, practicePlayer.getAutoKit().get().getName());
        }

        //Teleport the practice player to the spawn
        practicePlayer.teleportToTheSpawn();
    }

    /**
     * Listen when a player respawn
     *
     * @param e the player respawn event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() instanceof Player) {
            PracticePlayer practiceVictim = PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId());
            PracticePlayer practiceKiller = PracticePlayerManager.getPracticePlayer(e.getEntity().getKiller().getUniqueId());

            //Increases deaths and kills values
            practiceVictim.addDeath();
            practiceKiller.addKill();

        }
    }

}
