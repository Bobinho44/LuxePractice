package fr.bobinho.luxepractice.utils.arena.match;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Sets;
import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class PracticeMatch {

    /**
     * Fields
     */
    private final PracticeArena arena;
    private final Set<PracticePlayer> spectators = new HashSet<>();
    private final List<PracticePlayer> fighters = new ArrayList<>();
    private final Set<PracticePlayer> deadFighter = new HashSet<>();
    private final Stopwatch duration;
    private boolean isFinished = false;

    /**
     * Creates a new practice match
     *
     * @param arena the practice arena
     */
    public PracticeMatch(@Nonnull PracticeArena arena) {
        Validate.notNull(arena, "arena is null");

        this.arena = arena;
        this.duration = Stopwatch.createStarted();
    }

    /**
     * Gets the practice arena
     *
     * @return the practice arena
     */
    @Nonnull
    public PracticeArena getArena() {
        return arena;
    }

    /**
     * Gets the practice match spectators
     *
     * @return the practice match spectators
     */
    @Nonnull
    public Set<PracticePlayer> getSpectators() {
        return spectators;
    }

    /**
     * Checks if the practice player is a practice match spectator
     *
     * @param practicePlayer the practice player
     * @return if he is a practice match spectator
     */
    public boolean isItSpectator(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(PracticeMatchManager.isInMatch(practicePlayer), "practicePlayer is not in a match");

        return getSpectators().contains(practicePlayer);
    }

    /**
     * Adds a new practice player to the practice match spectators
     *
     * @param practicePlayer the practice player
     */
    public void addSpectator(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        getSpectators().add(practicePlayer);
    }

    /**
     * Removes the practice player from the practice match spectators
     *
     * @param practicePlayer the practice player
     */
    public void removeSpectator(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(PracticeMatchManager.isInMatch(practicePlayer), "practicePlayer is not in a match");

        getSpectators().remove(practicePlayer);
    }

    /**
     * Gets the practice match fighter
     *
     * @return the practice match fighters
     */
    @Nonnull
    public List<PracticePlayer> getFighters() {
        return fighters;
    }

    /**
     * Checks if the practice player is a practice match fighters
     *
     * @param practicePlayer the practice player
     * @return if he is a practice match fighters
     */
    public boolean isItFighter(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(PracticeMatchManager.isInMatch(practicePlayer), "practicePlayer is not in a match");

        return getFighters().contains(practicePlayer);
    }

    /**
     * Adds a new practice player to the practice match fighters
     *
     * @param practicePlayer the practice player
     */
    public void addFighter(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        getFighters().add(practicePlayer);
    }

    /**
     * Gets the practice match dead fighter
     *
     * @return the practice match dead fighters
     */
    @Nonnull
    public Set<PracticePlayer> getDeadFighters() {
        return deadFighter;
    }

    /**
     * Adds a new practice player to the practice match dead fighters
     *
     * @param practicePlayer the practice player
     */
    public void addDeadFighter(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        getDeadFighters().add(practicePlayer);
    }

    /**
     * Gets the practice match duration
     *
     * @return the practice match duration
     */
    @Nonnull
    public Stopwatch getDuration() {
        return duration;
    }

    /**
     * Checks if the practice match is finished
     *
     * @return if it is finished
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * Starts the practice match
     */
    public void start() {
        PracticeScheduler.syncScheduler().after(30, TimeUnit.MINUTES).run(() -> {

            //Checks if the practice match is finish
            if (mustFinish()) {

                //Finishes the practice match
                finish();
            }
        });
    }

    /**
     * Finishes the practice match
     */
    public void finish() {
        isFinished = true;
        PracticeMatchManager.sendMessageToAllPlayerInMatch(getFinishMessage(), this);
        PracticeMatchManager.sendMessageToAllPlayerOutMatch(getBroadcastFinishMessage(), this);
        getFighters().stream().filter(practiceFighter -> !isItSpectator(practiceFighter)).collect(Collectors.toList())
                .forEach(practiceFighter -> PracticeMatchManager.addSpectator(practiceFighter, practiceFighter));
    }

    /**
     * Gets all practice match members (fighters and spectators)
     *
     * @return all practice match members
     */
    @Nonnull
    public Set<PracticePlayer> getALlMembers() {
        return Sets.union(getSpectators(), Sets.difference(Set.copyOf(getFighters()), getDeadFighters()));
    }

    /**
     * Checks if the practice match must be finished
     *
     * @return if it must be finished
     */
    public abstract boolean mustFinish();

    /**
     * Gets the practice match start message
     *
     * @param practiceReceiver the practice receiver
     * @return the practice match start message
     */
    @Nonnull
    public abstract BaseComponent[] getStartMessage(@Nonnull PracticePlayer practiceReceiver);

    /**
     * Gets the practice match finish message
     *
     * @return the practice match finish message
     */
    @Nonnull
    public abstract BaseComponent[] getFinishMessage();

    /**
     * Gets the practice match finish broadcast message
     *
     * @return the practice match finish broadcast message
     */
    @Nonnull
    public abstract BaseComponent[] getBroadcastFinishMessage();

    /**
     * Gets the practice match informations as string
     *
     * @return the practice match informations as string
     */
    @Nonnull
    public abstract String getMatchInfoAsString();

}
