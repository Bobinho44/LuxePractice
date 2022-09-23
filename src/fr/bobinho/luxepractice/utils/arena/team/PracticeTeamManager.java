package fr.bobinho.luxepractice.utils.arena.team;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");

        return getPracticeTeams().stream().filter(team -> team.getMembers().contains(practicePlayer)).findFirst();
    }

    /**
     * Checks if the practice player is a leader of a practice team
     *
     * @param practicePlayer the practice player
     * @return if it is a practice team leader
     */
    public static boolean isItPracticeTeamLeader(@Nonnull PracticePlayer practicePlayer) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");

        return getPracticeTeam(practicePlayer).get().getLeader().equals(practicePlayer);
    }

    /**
     * Checks if the practice player is a member of a practice team
     *
     * @param practicePlayer the practice player
     * @return if it it a practice team member
     */
    public static boolean hasPracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");

        return getPracticeTeam(practicePlayer).isPresent();
    }

    /**
     * Creates a new practice team
     *
     * @param practicePlayer the practice team leader
     */
    public static void createPracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");

        getPracticeTeams().add(new PracticeTeam(practicePlayer));
    }

    /**
     * Deletes the practice team
     *
     * @param practicePlayer the practice team leader
     */
    public static void deletePracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");

        getPracticeTeams().remove(getPracticeTeam(practicePlayer).get());
    }

    /**
     * Joins a practice team
     *
     * @param practicePlayer the practice team member
     */
    public static void joinPracticeTeam(@Nonnull PracticePlayer practicePlayer, @Nonnull PracticeTeam practiceTeam) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");
        Objects.requireNonNull(practiceTeam, "practiceTeam is null");

        practiceTeam.addMember(practicePlayer);
    }

    /**
     * Leaves the practice team
     *
     * @param practicePlayer the practice team member
     */
    public static void leavePracticeTeam(@Nonnull PracticePlayer practicePlayer) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");

        getPracticeTeam(practicePlayer).get().removeMember(practicePlayer);
    }

    /**
     * Sends a message to all practice team member
     *
     * @param practicePlayer the practice sender
     * @param message        the message
     */
    public static void sendMessageToPracticeTeamMembers(@Nonnull PracticePlayer practicePlayer, @Nonnull String message) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");
        Objects.requireNonNull(message, "message is null");

        getPracticeTeam(practicePlayer).get().getMembers().forEach(practiceMember -> practiceMember.sendMessage(message));
    }

}
