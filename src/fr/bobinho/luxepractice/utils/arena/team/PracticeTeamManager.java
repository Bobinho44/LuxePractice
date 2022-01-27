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

    public static void createPracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(!hasPracticeTeam(practicePlayer), "practicePlayer has already a practice team");

        getPracticeTeams().add(new PracticeTeam(practicePlayer));
    }

    public static void deletePracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(hasPracticeTeam(practicePlayer), "practicePlayer doesn't have a practice team");

        getPracticeTeams().remove(getPracticePlayerTeam(practicePlayer).get());
    }

    public static void leavePracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(hasPracticeTeam(practicePlayer), "practicePlayer doesn't have a practice team");

       getPracticePlayerTeam(practicePlayer).get().removeMember(practicePlayer);
    }

}
