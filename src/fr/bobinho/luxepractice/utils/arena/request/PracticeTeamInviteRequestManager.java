package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PracticeTeamInviteRequestManager {

    /**
     * The practice team invite requests list
     */
    private static final List<PracticeRequest> practiceTeamInviteRequests = new ArrayList<>();

    /**
     * Gets all practice team invite request
     *
     * @return the practice team invite requests
     */
    private static List<PracticeRequest> getPracticeTeamInviteRequests() {
        return practiceTeamInviteRequests;
    }

    /**
     * Gets a specific practice team invite request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the practice team invite request if found
     */
    private static Optional<PracticeRequest> getPracticeTeamInviteRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");

        //Gets the selected practice team invite request
        return getPracticeTeamInviteRequests().stream().filter(request -> request.getPracticeSender().equals(practiceSender) && request.getPracticeReceiver().equals(practiceReceiver)).findFirst();
    }

    /**
     * Checks if a specific practice team invite request exist
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the status of the existence of the practice team invite request
     */
    public static boolean isItPracticeTeamInviteRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");

        //Checks if the select practice team invite request exist
        return getPracticeTeamInviteRequest(practiceSender, practiceReceiver).isPresent();
    }

    /**
     * Sends a practice team invite request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     */
    public static void sendPracticeTeamInviteRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");
        Validate.isTrue(!isItPracticeTeamInviteRequest(practiceSender, practiceReceiver), "this request has already been sent");

        //Creates the practice team invite request
        getPracticeTeamInviteRequests().add(new PracticeRequest(practiceSender, practiceReceiver));

        //Waits 60 seconds to clear the practice team invite request
        PracticeScheduler.syncScheduler().after(60, TimeUnit.SECONDS).run(() -> {

            //Checks if the practice team invite request still exist
            if (isItPracticeTeamInviteRequest(practiceSender, practiceReceiver)) {

                //Removes the practice team invite request
                removePracticeTeamInviteRequest(practiceSender, practiceReceiver);
            }
        });
    }

    /**
     * Removes a practice team invite request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     */
    public static void removePracticeTeamInviteRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceSender, "practiceSender is null");
        Validate.notNull(practiceReceiver, "practiceReceiver is null");
        Validate.isTrue(isItPracticeTeamInviteRequest(practiceSender, practiceReceiver), "this request doesn't exist");

        //Removes the practice team invite request
        getPracticeTeamInviteRequests().remove(getPracticeTeamInviteRequest(practiceSender, practiceReceiver).get());
    }

}