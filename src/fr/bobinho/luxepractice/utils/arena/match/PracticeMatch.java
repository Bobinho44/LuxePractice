package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.chat.BaseComponent;
import org.atlanmod.commons.Guards;
import org.atlanmod.commons.time.Stopwatch;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class PracticeMatch {

    private final PracticeArena arena;
    private Stopwatch duration;

    protected PracticeMatch(@Nonnull PracticeArena arena) {
        Guards.checkNotNull(arena, "arena is null");

        this.arena = arena;
        this.duration = Stopwatch.createStarted();
    }

    public PracticeArena getArena() {
        return arena;
    }

    public Stopwatch getDuration() {
        return duration;
    }

    public abstract BaseComponent[] getStartMessageForFighters(@Nonnull PracticePlayer receiver);

    public abstract BaseComponent[] getEndMessageForFighters(@Nonnull PracticePlayer receiver);

    public abstract BaseComponent[] getEndMessageForEveryone(@Nonnull PracticePlayer receiver);

    public abstract List<PracticePlayer> getALlMembers();

}
