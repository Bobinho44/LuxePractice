package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PracticeDuelRequestManager {

    /**
     * The practice duel requests list
     */
    private static final List<PracticeRequest> practiceDuelRequests = new ArrayList<>();

    /**
     * Gets all practice duel request
     *
     * @return the practice duel requests
     */
    private static List<PracticeRequest> getPracticeDuelRequests() {
        return practiceDuelRequests;
    }

    /**
     * Gets a specific practice duel request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the practice duel request if found
     */
    private static Optional<PracticeRequest> getPracticeDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");

        //Gets the selected practice duel request
        return getPracticeDuelRequests().stream().filter(request -> request.getPracticeSender().equals(practiceSender) && request.getPracticeReceiver().equals(practiceReceiver)).findFirst();
    }

    /**
     * Checks if a specific practice duel request exist
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the status of the existence of the practice duel request
     */
    public static boolean isItPracticeDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");

        //Checks if the select practice duel request exist
        return getPracticeDuelRequest(practiceSender, practiceReceiver).isPresent();
    }

    /**
     * Sends a practice duel request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     */
    public static void sendPracticeDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");
        Validate.isTrue(!isItPracticeDuelRequest(practiceSender, practiceReceiver), "this request has already been sent");

        //Creates the practice duel request
        getPracticeDuelRequests().add(new PracticeRequest(practiceSender, practiceReceiver));

        //Waits 60 seconds to clear the practice duel request
        PracticeScheduler.syncScheduler().after(60, TimeUnit.SECONDS).run(() -> {

            //Checks if the practice duel request still exist
            if (isItPracticeDuelRequest(practiceSender, practiceReceiver)) {

                //Removes the practice duel request
                removePracticeDuelRequest(practiceSender, practiceReceiver);
            }
        });
    }

    /**
     * Removes a practice duel request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     */
    public static void removePracticeDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");
        Validate.isTrue(isItPracticeDuelRequest(practiceSender, practiceReceiver), "this request doesn't exist");

        //Removes the practice duel request
        getPracticeDuelRequests().remove(getPracticeDuelRequest(practiceSender, practiceReceiver).get());
    }

}