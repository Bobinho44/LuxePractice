package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PracticeTeamDuelRequestManager {

    /**
     * The practice team duel requests list
     */
    private static final List<PracticeRequest> practiceTeamDuelRequests = new ArrayList<>();

    /**
     * Gets all practice team duel request
     *
     * @return the practice team duel requests
     */
    private static List<PracticeRequest> getPracticeTeamDuelRequests() {
        return practiceTeamDuelRequests;
    }

    /**
     * Gets a specific practice team duel request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the practice team duel request if found
     */
    private static Optional<PracticeRequest> getPracticeTeamDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Objects.requireNonNull(practiceSender, "practiceSender is null");
        Objects.requireNonNull(practiceReceiver, "practiceReceiver is null");

        //Gets the selected practice team duel request
        return getPracticeTeamDuelRequests().stream().filter(request -> request.getPracticeSender().equals(practiceSender) && request.getPracticeReceiver().equals(practiceReceiver)).findFirst();
    }

    /**
     * Checks if a specific practice team duel request exist
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the status of the existence of the practice team duel request
     */
    public static boolean isItPracticeTeamDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Objects.requireNonNull(practiceSender, "practiceSender is null");
        Objects.requireNonNull(practiceReceiver, "practiceReceiver is null");

        //Checks if the select practice team duel request exist
        return getPracticeTeamDuelRequest(practiceSender, practiceReceiver).isPresent();
    }

    /**
     * Sends a practice team duel request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     */
    public static void sendPracticeTeamDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Objects.requireNonNull(practiceSender, "practiceSender is null");
        Objects.requireNonNull(practiceReceiver, "practiceReceiver is null");

        //Creates the practice team duel request
        getPracticeTeamDuelRequests().add(new PracticeRequest(practiceSender, practiceReceiver));

        //Waits 60 seconds to clear the practice team duel request
        PracticeScheduler.syncScheduler().after(60, TimeUnit.SECONDS).run(() -> {

            //Checks if the practice team duel request still exist
            if (isItPracticeTeamDuelRequest(practiceSender, practiceReceiver)) {

                //Removes the practice team duel request
                removePracticeTeamDuelRequest(practiceSender, practiceReceiver);
            }
        });
    }

    /**
     * Removes a practice team duel request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     */
    public static void removePracticeTeamDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Objects.requireNonNull(practiceSender, "practiceSender is null");
        Objects.requireNonNull(practiceReceiver, "practiceReceiver is null");

        //Removes the practice team duel request
        getPracticeTeamDuelRequests().remove(getPracticeTeamDuelRequest(practiceSender, practiceReceiver).get());
    }

}
