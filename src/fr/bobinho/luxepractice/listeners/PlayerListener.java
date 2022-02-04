package fr.bobinho.luxepractice.listeners;

import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeWaitListManager;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
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
        PracticePlayerManager.loadPracticePlayerData(e.getPlayer());

        //Teleport the player to the spawn
        PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId()).ifPresent(PracticePlayer::teleportToTheSpawn);
    }

    /**
     * Listen when a player leave the server
     *
     * @param e the player quit event
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId()).ifPresent(practicePlayer -> {

            //Checks if the practice player is a leader of a practice team
            if (PracticeTeamManager.isItPracticeTeamLeader(practicePlayer)) {
                PracticeTeamManager.deletePracticeTeam(practicePlayer);
            }

            //Checks if the player is in a match
            if (PracticeMatchManager.isInMatch(practicePlayer)) {
                PracticeMatch practiceMatch = PracticeMatchManager.getPracticeMatch(practicePlayer).get();

                //Checks if the player is not spectator
                if (!practiceMatch.isItSpectator(practicePlayer)) {

                    //Kills the player
                    PracticeMatchManager.addSpectator(practicePlayer, practicePlayer);
                }
                PracticeMatchManager.removeSpectator(practicePlayer);
            }

            if (PracticeWaitListManager.isAlreadyInThePracticeWaitList(practicePlayer)) {
                PracticeWaitListManager.removePracticePlayerToTheWaitList(practicePlayer);
            }

            //Saves player's data
            PracticePlayerManager.savePracticePlayerData(e.getPlayer().getUniqueId());
            PracticePlayerManager.unregisterPracticePlayer(practicePlayer);
        });
    }

    /**
     * Listen when a player respawn
     *
     * @param e the player respawn event
     */
    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        PracticePlayerManager.getPracticePlayer(e.getPlayer().getUniqueId()).ifPresent(practicePlayer -> {

            if (PracticeMatchManager.isInMatch(practicePlayer)) {
                PracticeMatchManager.addSpectator(practicePlayer, practicePlayer);
                return;
            }

            //Checks if the player has enabled an auto kit
            if (PracticeKitManager.haveAutoPracticeKit(practicePlayer)) {

                //Gives the auto kit
                PracticeKitManager.givePracticeKit(practicePlayer, practicePlayer.getAutoKit().get().getName());
            }

            //Teleport the practice player to the spawn
            practicePlayer.teleportToTheSpawn();
        });
    }

    /**
     * Listen when a player respawn
     *
     * @param e the player respawn event
     */
    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {

            //Increases deaths and kills values
            PracticePlayerManager.getPracticePlayer(e.getEntity().getUniqueId()).ifPresent(PracticePlayer::addDeath);
            PracticePlayerManager.getPracticePlayer(e.getEntity().getKiller().getUniqueId()).ifPresent(PracticePlayer::addKill);
        }
    }

}
