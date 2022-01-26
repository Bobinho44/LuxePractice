package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
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
    public static boolean isInMatch(@Nonnull PracticePlayer practicePlayer) {
        return getMatchs().stream().anyMatch(match -> match.getALlMembers().contains(practicePlayer));
    }

    public static List<PracticeArena> getUsedPracticeArenas() {
        return getMatchs().stream().map(PracticeMatch::getArena).collect(Collectors.toList());
    }

    public static void sendMessageToAllPlayerInMatch(BaseComponent[] message, PracticeMatch practiceMatch) {
        for (PracticePlayer practicePlayer : practiceMatch.getALlMembers()) {
            practicePlayer.getSpigotPlayer().get().sendMessage(message);
        }
    }

    public static void addSpectator(@Nonnull PracticePlayer practiceViewer, @Nonnull PracticePlayer practiceStreamer) {
        Guards.checkNotNull(practiceStreamer, "practiceStreamer is null");
        Guards.checkNotNull(practiceViewer, "practiceViewer is null");
        Guards.checkArgument(!isInMatch(practiceViewer), "practiceViewer is already in a match");
        Guards.checkArgument(isInMatch(practiceStreamer), "practiceStreamer is not in a match");

        PracticeMatch practiceMatch = getMatch(practiceStreamer).get();

        practiceMatch.addSpectator(practiceViewer);
        for (PracticePlayer member1 : practiceMatch.getALlMembers()) {
            for (PracticePlayer member2 : practiceMatch.getALlMembers()) {
                if (!practiceMatch.isSpectator(member1) && !member1.equals(member2)) {
                    member1.getSpigotPlayer().get().hidePlayer(LuxePracticeCore.getInstance(), member2.getSpigotPlayer().get());
                }
                if (practiceMatch.isSpectator(member1) && practiceMatch.isSpectator(member2) && !member1.equals(member2)) {
                    member1.getSpigotPlayer().get().hidePlayer(LuxePracticeCore.getInstance(), member2.getSpigotPlayer().get());
                }
            }
        }
        practiceViewer.getSpigotPlayer().get().setAllowFlight(true);
    }

    public static void removeSpectator(@Nonnull PracticePlayer practiceViewer) {
        Guards.checkNotNull(practiceViewer, "practiceViewer is null");
        Guards.checkArgument(isInMatch(practiceViewer), "practiceViewer is not in a match");

        PracticeMatch practiceMatch = getMatch(practiceViewer).get();

        practiceMatch.removeSpectator(practiceViewer);
        Bukkit.getOnlinePlayers().forEach(player -> practiceViewer.getSpigotPlayer().get().showPlayer(LuxePracticeCore.getInstance(), player));
        practiceViewer.getSpigotPlayer().get().setAllowFlight(false);
    }
}
