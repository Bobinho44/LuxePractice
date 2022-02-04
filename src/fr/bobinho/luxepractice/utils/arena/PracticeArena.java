package fr.bobinho.luxepractice.utils.arena;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;

import javax.annotation.Nonnull;

public class PracticeArena {

    /**
     * Fields
     */
    private final Location spawn;
    private final String name;

    /**
     * Creates a new practice arena
     * @param spawn the practice arena spawn
     * @param name the practice arena name
     */
    public PracticeArena(@Nonnull Location spawn, @Nonnull String name) {
        Validate.notNull(spawn, "spawn is null");
        Validate.notNull(name, "name is null");

        this.spawn = spawn;
        this.name = name;
    }

    /**
     * Gets the practice arena name
     * @return the practice arena name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the practice arena spawn
     * @return the practice arena spawn
     */
    public Location getSpawn() {
        return spawn;
    }

}
