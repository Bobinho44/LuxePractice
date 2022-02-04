package fr.bobinho.luxepractice.utils.arena.request;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PracticeWaitListManager {

    /**
     * The practice wait lists
     */
    private final static Map<String, Queue<PracticePlayer>> practiceWaitList = new HashMap<>();

    /**
     * Gets all practice wait lists
     *
     * @return all practice wait lists
     */
    @Nonnull
    private static Map<String, Queue<PracticePlayer>> getPracticeWaitList() {
        return practiceWaitList;
    }

    /**
     * Gets a practice wait list
     *
     * @param kitname the practice kit name associated with the practice wait list
     * @return the practice wait list
     */
    @Nullable
    public static Queue<PracticePlayer> getWaitList(@Nonnull String kitname) {
        Validate.notNull(kitname, "kitname is null");

        return getPracticeWaitList().get(kitname.toLowerCase());
    }

    /**
     * Gets a practice player from a practice wait list
     *
     * @param kitname the practice kit name associated with the practice wait list
     * @return the practice player
     */
    @Nonnull
    public static Optional<PracticePlayer> getPracticePlayerFromTheWaitList(@Nonnull String kitname) {
        Validate.notNull(kitname, "kitname is null");

        return Optional.ofNullable(getWaitList(kitname) == null ? null : getWaitList(kitname).poll());
    }

    /**
     * Checks if a practice player is in a practice wait list
     *
     * @param practicePlayer the practice player
     * @return if it is in a practice wait list
     */
    public static boolean isAlreadyInThePracticeWaitList(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        return getPracticeWaitList().values().stream().anyMatch(kit -> kit.contains(practicePlayer));
    }

    /**
     * Checks if there is a practice player in a wait list
     *
     * @param kitname the practice kit name associated with the practice wait list
     * @return if thee is a practice player in a wait list
     */
    public static boolean isThereAvailablePracticePlayer(@Nonnull String kitname) {
        Validate.notNull(kitname, "kitname is null");

        return getWaitList(kitname) != null && !getWaitList(kitname).isEmpty();
    }

    /**
     * Adds a practice player to a wait list
     *
     * @param practicePlayer the practice player
     * @param kitname        the practice kit name associated with the practice wait list
     */
    public static void addPracticePlayerToTheWaitList(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitname) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(kitname, "kitname is null");
        Validate.isTrue(!isAlreadyInThePracticeWaitList(practicePlayer), "practicePlayer is already in the wait list");

        //Adds the practice player to a new practice wait list
        if (getWaitList(kitname) == null) {
            getPracticeWaitList().put(kitname.toLowerCase(), new LinkedList<>());
        }

        //Adds the practice player to the practice wait list
        getWaitList(kitname).offer(practicePlayer);
    }

    /**
     * Removes the practice player from the wait list
     *
     * @param practicePlayer the practice player
     */
    public static void removePracticePlayerToTheWaitList(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(isAlreadyInThePracticeWaitList(practicePlayer), "practicePlayer is not in a the wait list");

        getPracticeWaitList().values().stream().filter(kit -> kit.contains(practicePlayer)).findFirst().get().remove(practicePlayer);
    }

}