package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;

import javax.annotation.Nonnull;
import java.util.*;

public class PracticeWaitList {

    private static Map<String, Queue<PracticePlayer>> waitList = new HashMap<>();

    private static Queue<PracticePlayer> getWaitList(@Nonnull String kitname) {
        Guards.checkNotNull(kitname, "kitname is null");

        return waitList.get(kitname.toLowerCase());
    }

    public static boolean isAlreadyInThePracticeWaitList(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return waitList.values().stream().anyMatch(kit -> kit.contains(practicePlayer));
    }

    public static void addPracticePlayerToTheWaitList(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitname) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitname, "kitname is null");
        Guards.checkArgument(!isAlreadyInThePracticeWaitList(practicePlayer), "practicePlayer is already in the wait list");

        if (getWaitList(kitname) == null) {
            waitList.put(kitname.toLowerCase(), new LinkedList<>());
        }
        getWaitList(kitname).offer(practicePlayer);
    }

    public static Optional<PracticePlayer> getPracticePlayerFromTheWaitList(@Nonnull String kitname) {
        Guards.checkNotNull(kitname, "kitname is null");

        if (getWaitList(kitname) == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(getWaitList(kitname).poll());
    }

    public static void removePracticePlayerToTheWaitList(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitname) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitname, "kitname is null");
        Guards.checkArgument(isAlreadyInThePracticeWaitList(practicePlayer), "practicePlayer is not in a wait list");

        getWaitList(kitname).remove(practicePlayer);
    }

    public static void removePracticePlayerToTheWaitList(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(isAlreadyInThePracticeWaitList(practicePlayer), "practicePlayer is not in a the wait list");

        waitList.values().stream().filter(kit -> kit.contains(practicePlayer)).findFirst().get().remove(practicePlayer);
    }

    public static boolean isThereAvailablePracticePlayer(@Nonnull String kitname) {
        Guards.checkNotNull(kitname, "kitname is null");

        return getWaitList(kitname) != null && !getWaitList(kitname).isEmpty();
    }

}
