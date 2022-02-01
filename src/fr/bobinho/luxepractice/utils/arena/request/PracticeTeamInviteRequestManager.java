package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.arena.request.PracticeRequest;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import org.atlanmod.commons.Guards;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class PracticeTeamInviteRequestManager {

    private static final List<PracticeRequest> teamInviteRequests = new ArrayList<>();

    /**
     * Gets all practice team invite request
     *
     * @return the practice team invite requests
     */
    private static List<PracticeRequest> getTeamInviteRequests() {
        return teamInviteRequests;
    }

    /**
     * Gets a specific practice team invite request
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the practice team invite request if found
     */
    private static Optional<PracticeRequest> getPracticeTeamInviteRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Guards.checkNotNull(practiceSender, "practiceSender is null");
        Guards.checkNotNull(practiceReceiver, "practiceReceiver is null");

        //Gets the selected practice team invite request
        return getTeamInviteRequests().stream().filter(request -> request.getPracticeSender().equals(practiceSender) && request.getPracticeSender().equals(practiceReceiver)).findFirst();
    }

    /**
     * Checks if a specific practice team invite request exist
     *
     * @param practiceSender   the practice sender
     * @param practiceReceiver the practice receiver
     * @return the status of the existence of the practice team invite request
     */
    public static boolean isItPracticeTeamInviteRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Guards.checkNotNull(practiceSender, "practiceSender is null");
        Guards.checkNotNull(practiceReceiver, "practiceReceiver is null");

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
        Guards.checkNotNull(practiceSender, "practiceSender is null");
        Guards.checkNotNull(practiceReceiver, "practiceReceiver is null");
        Guards.checkArgument(!isItPracticeTeamInviteRequest(practiceSender, practiceReceiver), "this request has already been sent");

        //Creates the practice team invite request
        getTeamInviteRequests().add(new PracticeRequest(practiceSender, practiceReceiver));

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
        Guards.checkNotNull(practiceSender, "practiceSender is null");
        Guards.checkNotNull(practiceReceiver, "practiceReceiver is null");
        Guards.checkArgument(isItPracticeTeamInviteRequest(practiceSender, practiceReceiver), "this request doesn't exist");

        //Removes the practice team invite request
        getTeamInviteRequests().remove(getPracticeTeamInviteRequest(practiceSender, practiceReceiver).get());
    }

}
