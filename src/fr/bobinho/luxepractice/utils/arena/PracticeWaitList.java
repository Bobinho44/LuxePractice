package fr.bobinho.luxepractice.utils.arena;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class PracticeWaitList {

    private static Queue<PracticePlayer> waitList = new LinkedList<PracticePlayer>();

    private static Queue<PracticePlayer> getWaitList() {
        return waitList;
    }

    public static boolean isAlreadyInThePracticeWaitList(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return getWaitList().contains(practicePlayer);
    }

    public static void addPracticePlayerToTheWaitList(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(!isAlreadyInThePracticeWaitList(practicePlayer), "practicePlayer is already in the wait list");

        getWaitList().offer(practicePlayer);
    }

    public static Optional<PracticePlayer> getPracticePlayerFromTheWaitList() {
        return Optional.of(getWaitList().poll());
    }

    public static boolean isThereAvailablePracticePlayer() {
        return getPracticePlayerFromTheWaitList().isPresent();
    }

}
