package fr.bobinho.luxepractice.utils.arena.team;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PracticeTeamManager {

    private static List<PracticeTeam> teams = new ArrayList<PracticeTeam>();

    private static List<PracticeTeam> getPracticeTeams() {
        return teams;
    }

    public static boolean hasPracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return getPracticeTeams().stream().anyMatch(team -> team.getMembers().contains(practicePlayer));
    }

    public static Optional<PracticeTeam> getPracticePlayerTeam(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return getPracticeTeams().stream().filter(team -> team.getMembers().contains(practicePlayer)).findFirst();
    }

}
