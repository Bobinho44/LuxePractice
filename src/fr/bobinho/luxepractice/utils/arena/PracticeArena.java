package fr.bobinho.luxepractice.utils.arena;

import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PracticeArena {

    /**
     * Fields
     */
    private final Location spawn1;
    private final Location spawn2;
    private final String name;

    /**
     * Creates a new practice arena
     * @param spawn1 the practice arena spawn1
     * @param spawn2 the practice arena spawn2
     * @param name the practice arena name
     */
    public PracticeArena(@Nonnull Location spawn1, Location spawn2, @Nonnull String name) {
        Objects.requireNonNull(spawn1, "spawn1 is null");
        Objects.requireNonNull(spawn2, "spawn2 is null");
        Objects.requireNonNull(name, "name is null");

        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
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
     * Gets the practice arena spawn1
     * @return the practice arena spawn1
     */
    public Location getSpawn1() {
        return spawn1;
    }

    /**
     * Gets the practice arena spawn2
     * @return the practice arena spawn2
     */
    public Location getSpawn2() {
        return spawn2;
    }

}
