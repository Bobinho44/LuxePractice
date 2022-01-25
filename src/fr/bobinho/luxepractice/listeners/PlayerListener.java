package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

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
    }

    /**
     * Listen when a player leave the server
     *
     * @param e the player quit event
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {

        //Saves player's data
        PracticePlayerManager.savePracticePlayerData(e.getPlayer().getUniqueId());
    }

    /**
     * Listen when a player respawn
     *
     * @param e the player respawn event
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId());

        //Checks if the player has enabled an auto kit
        if (PracticeKitManager.haveAutoPracticeKit(practicePlayer)) {

            //Gives the auto kit
            PracticeKitManager.givePracticeKit(practicePlayer, practicePlayer.getAutoKit().get().getName());
        }
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
            practiceKiller.getKills();

        }
    }

}
