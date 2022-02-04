package fr.bobinho.luxepractice.utils.arena.team;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PracticeTeamManager {

    /**
     * The practice teams list
     */
    private final static List<PracticeTeam> practiceTeams = new ArrayList<>();

    /**
     * Gets all practice teams
     *
     * @return all practice teams
     */
    @Nonnull
    public static List<PracticeTeam> getPracticeTeams() {
        return practiceTeams;
    }

    /**
     * Gets a practice team
     *
     * @param practicePlayer a practice team member
     * @return the practice team
     */
    @Nonnull
    public static Optional<PracticeTeam> getPracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        return getPracticeTeams().stream().filter(team -> team.getMembers().contains(practicePlayer)).findFirst();
    }

    /**
     * Checks if the practice player is a leader of a practice team
     *
     * @param practicePlayer the practice player
     * @return if it is a practice team leader
     */
    public static boolean isItPracticeTeamLeader(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(hasPracticeTeam(practicePlayer), "practicePlayer doesn't have a practice team");

        return getPracticeTeam(practicePlayer).get().getLeader().equals(practicePlayer);
    }

    /**
     * Checks if the practice player is a member of a practice team
     *
     * @param practicePlayer the practice player
     * @return if it it a practice team member
     */
    public static boolean hasPracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        return getPracticeTeam(practicePlayer).isPresent();
    }

    /**
     * Creates a new practice team
     *
     * @param practicePlayer the practice team leader
     */
    public static void createPracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(!hasPracticeTeam(practicePlayer), "practicePlayer has already a practice team");

        getPracticeTeams().add(new PracticeTeam(practicePlayer));
    }

    /**
     * Deletes the practice team
     *
     * @param practicePlayer the practice team leader
     */
    public static void deletePracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(isItPracticeTeamLeader(practicePlayer), "practicePlayer is not a practice team leader");

        getPracticeTeams().remove(getPracticeTeam(practicePlayer).get());
    }

    /**
     * Joins a practice team
     *
     * @param practicePlayer the practice team member
     */
    public static void joinPracticeTeam(@Nonnull PracticePlayer practicePlayer, @Nonnull PracticeTeam practiceTeam) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(practiceTeam, "practiceTeam is null");
        Validate.isTrue(!hasPracticeTeam(practicePlayer), "practicePlayer already have a practice team");

        practiceTeam.addMember(practicePlayer);
    }

    /**
     * Leaves the practice team
     *
     * @param practicePlayer the practice team member
     */
    public static void leavePracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(hasPracticeTeam(practicePlayer), "practicePlayer doesn't have a practice team");

        getPracticeTeam(practicePlayer).get().removeMember(practicePlayer);
    }

    /**
     * Sends a message to all practice team member
     *
     * @param practicePlayer the practice sender
     * @param message        the message
     */
    public static void sendMessageToPracticeTeamMembers(@Nonnull PracticePlayer practicePlayer, @Nonnull String message) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(message, "message is null");
        Validate.isTrue(hasPracticeTeam(practicePlayer), "practicePlayer doesn't have a practice team");

        getPracticeTeam(practicePlayer).get().getMembers().forEach(practiceMember -> practiceMember.sendMessage(message));
    }

}