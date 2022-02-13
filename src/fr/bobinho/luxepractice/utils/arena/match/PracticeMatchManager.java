package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeDuelRequestManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeTeamDuelRequestManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeWaitListManager;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class PracticeMatchManager {

    /**
     * The practice matchs list
     */
    private final static List<PracticeMatch> practiceMatches = new ArrayList<>();

    /**
     * Gets all practice matches
     *
     * @return all practice matches
     */
    @Nonnull
    private static List<PracticeMatch> getPracticeMatches() {
        return practiceMatches;
    }

    /**
     * Gets a practice match with the practice player as member
     *
     * @param practicePlayer the practice player
     * @return the practice match
     */
    @Nonnull
    public static Optional<PracticeMatch> getPracticeMatch(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        return getPracticeMatches().stream().filter(match -> match.getALlMembers().contains(practicePlayer)).findFirst();
    }

    /**
     * Gets a practice match with the practice arena
     *
     * @param practiceArena the practice arena
     * @return the practice match
     */
    @Nonnull
    public static Optional<PracticeMatch> getPracticeMatch(@Nonnull PracticeArena practiceArena) {
        Validate.notNull(practiceArena, "practiceArena is null");

        return getPracticeMatches().stream().filter(match -> match.getArena().equals(practiceArena)).findFirst();
    }

    /**
     * Checks if a practice player is in a practice match
     *
     * @param practicePlayer the practice player
     * @return if he is in a practice match
     */
    public static boolean isInMatch(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        return getPracticeMatches().stream().anyMatch(match -> match.getALlMembers().contains(practicePlayer));
    }

    /**
     * Checks if a practice team have a practice member in a practice match
     *
     * @param practicePlayer a practice member of a practice team
     * @return if there is a practice team member in a practice match
     */
    public static boolean practiceTeamMemberIsInMatch(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(PracticeTeamManager.hasPracticeTeam(practicePlayer), "practicePlayer doesn't have team");

        return PracticeTeamManager.getPracticeTeam(practicePlayer).get().getMembers().stream().anyMatch(PracticeMatchManager::isInMatch);
    }

    /**
     * Creates a new practice anonymous match
     *
     * @param matchFighter1 the practice fighter 1
     * @param kitName       the practice kit name
     */
    public static void createPracticeAnonymousMatch(@Nonnull PracticePlayer matchFighter1, @Nonnull String kitName) {
        Validate.notNull(matchFighter1, "matchFighter1 is null");
        Validate.notNull(kitName, "kitName is null");
        Validate.isTrue(PracticeKitManager.isItBasicPracticeKit(kitName), "kitName is not a valid kit name");

        //Gets all practice match informations
        PracticePlayer matchFighter2 = PracticeWaitListManager.getPracticePlayerFromTheWaitList(kitName).get();
        PracticeKit matchKit = PracticeKitManager.getBasicPracticeKit(kitName).get();

        //Creates a new anonymous practice match
        getPracticeMatches().add(new AnonymousMatch(PracticeArenaManager.getFreePracticeArena().get(), matchFighter1, matchFighter2, matchKit));
    }

    /**
     * Creates a new practice duel match
     *
     * @param matchFighter1 the practice fighter 1
     * @param matchFighter2 the practice fighter 2
     */
    public static void createPracticeDuelMatch(@Nonnull PracticePlayer matchFighter1, @Nonnull PracticePlayer matchFighter2) {
        Validate.notNull(matchFighter1, "matchFighter1 is null");
        Validate.notNull(matchFighter2, "matchFighter2 is null");

        //Removes the practice duel request
        PracticeDuelRequestManager.removePracticeDuelRequest(matchFighter1, matchFighter2);

        //Gets all practice match informations
        PracticeKit matchKit = new PracticeKit(matchFighter1.getName(), PracticeKitManager.getPracticePlayerInventoryAsKit(matchFighter1));

        //Creates a new duel practice match
        getPracticeMatches().add(new DuelMatch(PracticeArenaManager.getFreePracticeArena().get(), matchFighter1, matchFighter2, matchKit));
    }

    /**
     * Creates a new practice team fight match
     *
     * @param leaderMatchTeam1 the practice team 1 leader
     * @param leaderMatchTeam2 the practice team 2 leader
     */
    public static void createPracticeTeamDuelMatch(@Nonnull PracticePlayer leaderMatchTeam1, @Nonnull PracticePlayer leaderMatchTeam2) {
        Validate.notNull(leaderMatchTeam1, "leaderMatchTeam1 is null");
        Validate.notNull(leaderMatchTeam2, "leaderMatchTeam2 is null");

        //Removes the practice team duel request
        PracticeTeamDuelRequestManager.removePracticeTeamDuelRequest(leaderMatchTeam1, leaderMatchTeam2);

        //Gets all practice match informations
        boolean random = new Random().nextInt(2) != 0;
        PracticeTeam practiceBlueTeam = PracticeTeamManager.getPracticeTeam(random ? leaderMatchTeam1 : leaderMatchTeam2).get();
        PracticeTeam practiceRedTeam = PracticeTeamManager.getPracticeTeam(random ? leaderMatchTeam2 : leaderMatchTeam1).get();

        //Creates a new team duel practice match
        getPracticeMatches().add(new TeamDuel(PracticeArenaManager.getFreePracticeArena().get(), practiceBlueTeam, practiceRedTeam));
    }

    /**
     * Deletes the practice match
     *
     * @param practiceMatch the practice match
     */
    public static void deletePracticeMatch(@Nonnull PracticeMatch practiceMatch) {
        Validate.notNull(practiceMatch, "practiceMatch is null");
        Validate.isTrue(getPracticeMatches().contains(practiceMatch), "this match doesn't exist");

        getPracticeMatches().remove(practiceMatch);
    }

    /**
     * Adds a new practice spectator to a practice match
     *
     * @param practiceViewer   the practice spectator
     * @param practiceStreamer the practice fighter
     */
    public static void addSpectator(@Nonnull PracticePlayer practiceViewer, @Nonnull PracticePlayer practiceStreamer) {
        Validate.notNull(practiceStreamer, "practiceStreamer is null");
        Validate.notNull(practiceViewer, "practiceViewer is null");
        Validate.isTrue(isInMatch(practiceStreamer), "practiceStreamer is not in a match");

        PracticeMatch practiceMatch = getPracticeMatch(practiceStreamer).get();

        //Saves inventory of practice spectators (not old practice fighters)
        if (!PracticeMatchManager.isInMatch(practiceViewer)) {
            practiceViewer.saveOldInventory();
            practiceViewer.teleport(practiceMatch.getArena().getSpawn1());
        }

        //Adds practice player to the dead fighter list
        else {
            practiceMatch.addDeadFighter(practiceViewer);
        }

        //Adds all practice spectators attributes
        practiceMatch.addSpectator(practiceViewer);
        practiceViewer.changeName(null);
        practiceViewer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 1));
        practiceViewer.heal();
        practiceViewer.setAllowFlight(true);
        PracticeKitManager.giveSpectatorPracticeKit(practiceViewer);

        //Checks if the match must be finished
        if (practiceMatch.mustFinish()) {
            practiceMatch.finish();
        }
    }

    /**
     * Removes the practice spectator from a practice match
     *
     * @param practiceViewer the practice spectator
     */
    public static void removeSpectator(@Nonnull PracticePlayer practiceViewer) {
        Validate.notNull(practiceViewer, "practiceViewer is null");
        Validate.isTrue(isInMatch(practiceViewer), "practiceViewer is not in a match");

        PracticeMatch practiceMatch = getPracticeMatch(practiceViewer).get();

        //Removes all practice spectators attributes
        practiceMatch.removeSpectator(practiceViewer);
        practiceViewer.removeAllPotionEffects();
        practiceViewer.heal();
        practiceViewer.setAllowFlight(false);
        PracticeKitManager.givePracticeKit(practiceViewer, new PracticeKit("inventory", practiceViewer.getOldInventory()));
        practiceViewer.teleportToTheSpawn();

        //Checks if all practice match members are gone
        if (practiceMatch.isFinished() && practiceMatch.getSpectators().size() == 0) {
            deletePracticeMatch(practiceMatch);
        }
    }

    /**
     * Gets all practice arenas currently used in practice matches
     *
     * @return all practice arenas currently used in practice matches
     */
    @Nonnull
    public static List<PracticeArena> getUsedPracticeArenasInPracticeMatches() {
        return getPracticeMatches().stream().map(PracticeMatch::getArena).collect(Collectors.toList());
    }

    /**
     * Sends a message to all practice match members
     *
     * @param message       the message
     * @param practiceMatch the practice match
     */
    public static void sendMessageToAllPlayerInMatch(@Nonnull BaseComponent[] message, @Nonnull PracticeMatch practiceMatch) {
        Validate.notNull(message, "message is null");
        Validate.notNull(practiceMatch, "practiceMatch is null");

        //Sends the message to all practice match members
        for (PracticePlayer practicePlayer : practiceMatch.getALlMembers()) {
            practicePlayer.sendMessage(message);
        }
    }

    /**
     * Sends a message to all practice player out of the practice match
     *
     * @param message       the message
     * @param practiceMatch the practice match
     */
    public static void sendMessageToAllPlayerOutMatch(@Nonnull BaseComponent[] message, @Nonnull PracticeMatch practiceMatch) {
        Validate.notNull(message, "message is null");
        Validate.notNull(practiceMatch, "practiceMatch is null");

        //Sends the message to all practice player out of the practice match
        Bukkit.getOnlinePlayers().stream().map(player -> PracticePlayerManager.getPracticePlayer(player.getUniqueId()).get())
                .filter(practicePlayer -> !practiceMatch.getALlMembers().contains(practicePlayer))
                .forEach(practicePlayer -> practicePlayer.sendMessage(message));

    }

}