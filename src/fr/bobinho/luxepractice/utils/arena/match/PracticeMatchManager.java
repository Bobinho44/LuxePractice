package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PlayerConnection;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PracticeMatchManager {

    private static List<PracticeMatch> matchs = new ArrayList<PracticeMatch>();

    private static List<PracticeMatch> getMatchs() {
        return matchs;
    }

    public static Optional<PracticeMatch> getMatch(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return getMatchs().stream().filter(match -> match.getALlMembers().contains(practicePlayer)).findFirst();
    }

    public static void createPracticeMatch(@Nonnull PracticeMatch practiceMatch) {
        Guards.checkNotNull(practiceMatch, "practiceMatch is null");
        Guards.checkArgument(!getMatchs().contains(practiceMatch), "this match is already created");

        getMatchs().add(practiceMatch);
    }

    public static void deletePracticeMatch(@Nonnull PracticeMatch practiceMatch) {
        Guards.checkNotNull(practiceMatch, "practiceMatch is null");
        Guards.checkArgument(getMatchs().contains(practiceMatch), "this match doesn't exist");

        getMatchs().remove(practiceMatch);
    }

    public static boolean isInMatch(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return getMatchs().stream().anyMatch(match -> match.getALlMembers().contains(practicePlayer));
    }

    public static boolean practiceTeamMemberIsInMatch(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(PracticeTeamManager.hasPracticeTeam(practicePlayer), "practicePlayer doesn't have team");

        return PracticeTeamManager.getPracticePlayerTeam(practicePlayer).get().getMembers().stream().anyMatch(member -> isInMatch(member));
    }

    public static List<PracticeArena> getUsedPracticeArenas() {
        return getMatchs().stream().map(PracticeMatch::getArena).collect(Collectors.toList());
    }

    public static void sendMessageToAllPlayerInMatch(BaseComponent[] message, PracticeMatch practiceMatch) {
        for (PracticePlayer practicePlayer : practiceMatch.getALlMembers()) {
            practicePlayer.sendMessage(message);
        }
    }

    public static void addSpectator(@Nonnull PracticePlayer practiceViewer, @Nonnull PracticePlayer practiceStreamer) {
        Guards.checkNotNull(practiceStreamer, "practiceStreamer is null");
        Guards.checkNotNull(practiceViewer, "practiceViewer is null");
        Guards.checkArgument(isInMatch(practiceStreamer), "practiceStreamer is not in a match");

        PracticeMatch practiceMatch = getMatch(practiceStreamer).get();

        if (!practiceMatch.isDeadFighter(practiceViewer)) {
            Bukkit.getConsoleSender().sendMessage("SAAAVE");
            practiceViewer.saveOldInventory();
        }
        PracticeKitManager.giveSpectatorPracticeKit(practiceViewer);

        practiceMatch.addSpectator(practiceViewer);
        practiceViewer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999999, 1));
        practiceViewer.setAllowFlight(true);
        Bukkit.getConsoleSender().sendMessage("SPEECCC");
    }

    public static void addOldFighterAsSpectator(@Nonnull PracticePlayer practiceViewer) {
        Guards.checkNotNull(practiceViewer, "practiceViewer is null");
        Guards.checkArgument(isInMatch(practiceViewer), "practiceViewer is already in a match");

        addSpectator(practiceViewer, practiceViewer);

        practiceViewer.changeName(practiceViewer.getName());
    }

    public static void removeSpectator(@Nonnull PracticePlayer practiceViewer) {
        Guards.checkNotNull(practiceViewer, "practiceViewer is null");
        Guards.checkArgument(isInMatch(practiceViewer), "practiceViewer is not in a match");

        PracticeMatch practiceMatch = getMatch(practiceViewer).get();

        practiceMatch.removeSpectator(practiceViewer);
        practiceViewer.removeAllPotionEffects();
        practiceViewer.setAllowFlight(false);
    }

}
