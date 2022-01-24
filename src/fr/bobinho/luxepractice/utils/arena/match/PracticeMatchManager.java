package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PracticeMatchManager {

    private static List<PracticeMatch> matchs = new ArrayList<PracticeMatch>();

    private static List<PracticeMatch> getMatchs() {
        return matchs;
    }

    public static boolean isInMatch(@Nonnull PracticePlayer practicePlayer) {
        return getMatchs().stream().anyMatch(match -> match.getALlMembers().contains(practicePlayer));
    }
}
