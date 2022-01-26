package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.atlanmod.commons.Guards;
import org.atlanmod.commons.time.Stopwatch;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class PracticeMatch {

    private final PracticeArena arena;
    private final List<PracticePlayer> spectators;
    private final Stopwatch duration;

    protected PracticeMatch(@Nonnull PracticeArena arena) {
        Guards.checkNotNull(arena, "arena is null");

        this.arena = arena;
        this.duration = Stopwatch.createStarted();
        this.spectators = new ArrayList<PracticePlayer>();
    }

    public List<PracticePlayer> getSpectators() {
        return spectators;
    }

    public void addSpectator(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(!PracticeMatchManager.isInMatch(practicePlayer), "practicePlayer is already in a match");

        getSpectators().add(practicePlayer);
    }

    public void removeSpectator(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(PracticeMatchManager.isInMatch(practicePlayer), "practicePlayer is not in a match");

        getSpectators().remove(practicePlayer);
    }

    public boolean isSpectator(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(PracticeMatchManager.isInMatch(practicePlayer), "practicePlayer is not in a match");

        return getSpectators().contains(practicePlayer);
    }
    public PracticeArena getArena() {
        return arena;
    }

    public Stopwatch getDuration() {
        return duration;
    }

    public abstract BaseComponent[] getStartMessage(@Nonnull PracticePlayer receiver);

    public abstract BaseComponent[] getEndMessage(@Nonnull PracticePlayer receiver);

    public abstract BaseComponent[] getBroadcastMessage(@Nonnull PracticePlayer receiver);

    public abstract List<PracticePlayer> getALlMembers();

}
