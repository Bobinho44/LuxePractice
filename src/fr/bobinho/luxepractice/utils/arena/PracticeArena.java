package fr.bobinho.luxepractice.utils.arena;

import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import org.atlanmod.commons.Guards;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public class PracticeArena {

    private final Location spawn;
    private final String name;

    public PracticeArena(@Nonnull Location spawn, @Nonnull String name) {
        Guards.checkNotNull(spawn, "spawn is null");
        Guards.checkNotNull(name, "name is null");

        this.spawn = spawn;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Location getSpawn() {
        return spawn;
    }
    
}
