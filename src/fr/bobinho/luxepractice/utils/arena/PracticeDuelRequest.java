package fr.bobinho.luxepractice.utils.arena;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class PracticeDuelRequest {

    private static final Map<PracticePlayer, PracticePlayer> duelRequests = new HashMap<PracticePlayer, PracticePlayer>();

    private static Map<PracticePlayer, PracticePlayer> getDuelRequests() {
        return  duelRequests;
    }

    public static boolean hasSentPracticeDuelRequest(@Nonnull PracticePlayer practiceSender) {
        Guards.checkNotNull(practiceSender, "practiceSender is null");

        return getDuelRequests().containsKey(practiceSender);
    }

    public static boolean hasReceivedPracticeDuelRequest(@Nonnull PracticePlayer practiceReceiver, @Nonnull PracticePlayer practiceSender) {
        Guards.checkNotNull(practiceReceiver, "practiceReceiver is null");
        Guards.checkNotNull(practiceSender, "practiceSender is null");

        return getDuelRequests().get(practiceSender).equals(practiceReceiver);
    }

    public static void sendPracticeDuelRequest(@Nonnull PracticePlayer practiceSender, @Nonnull PracticePlayer practiceReceiver) {
        Guards.checkNotNull(practiceSender, "practiceSender is null");
        Guards.checkNotNull(practiceReceiver, "practiceReceiver is null");
        Guards.checkArgument(!hasSentPracticeDuelRequest(practiceSender), "practiceSender has already sent a request");

        duelRequests.put(practiceSender, practiceReceiver);
    }

    public static void removePracticeDuelRequest(@Nonnull PracticePlayer practiceSender) {
        Guards.checkNotNull(practiceSender, "practiceSender is null");
        Guards.checkArgument(hasSentPracticeDuelRequest(practiceSender), "practiceSender hasn't sent request");

        duelRequests.remove(practiceSender);
    }
}
