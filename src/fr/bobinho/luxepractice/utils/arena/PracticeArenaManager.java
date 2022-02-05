package fr.bobinho.luxepractice.utils.arena;

import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PracticeArenaManager {

    /**
     * The practice arenas list
     */
    private static final List<PracticeArena> practiceArenas = new ArrayList<>();

    /**
     * Gets all practice arenas
     *
     * @return all practice arenas
     */
    @Nonnull
    private static List<PracticeArena> getPracticeArenas() {
        return practiceArenas;
    }

    /**
     * Gets a practice arena
     *
     * @param arenaName the practice arena name
     * @return the practice arena
     */
    @Nonnull
    private static Optional<PracticeArena> getArena(@Nonnull String arenaName) {
        Validate.notNull(arenaName, "name is null");

        return getPracticeArenas().stream().filter(arena -> arena.getName().equalsIgnoreCase(arenaName)).findFirst();
    }

    /**
     * Checks if the name corresponds to a practice arename
     *
     * @param arenaName the practice arena name
     * @return if it is a practice arena
     */
    public static boolean isItPracticeArena(@Nonnull String arenaName) {
        return getArena(arenaName).isPresent();
    }

    /**
     * Creates a new practice arena
     *
     * @param arenaSpawn1 the practice arena spawn1
     * @param arenaSpawn2 the practice arena spawn2
     * @param arenaName  the practice arena name
     */
    public static void createPracticeArena(@Nonnull Location arenaSpawn1, @Nonnull Location arenaSpawn2, @Nonnull String arenaName) {
        Validate.notNull(arenaSpawn1, "arenaSpawn1 is null");
        Validate.notNull(arenaSpawn2, "arenaSpawn2 is null");
        Validate.notNull(arenaName, "arenaName is null");

        getPracticeArenas().add(new PracticeArena(arenaSpawn1, arenaSpawn2, arenaName));
    }

    public static void deletePracticeArena(@Nonnull String arenaName) {
        Validate.notNull(arenaName, "name is null");
        Validate.isTrue(isItPracticeArena(arenaName), "arenaName is not valid");

        getPracticeArenas().remove(getArena(arenaName).get());
    }

    /**
     * Gets a free practice arena
     *
     * @return the free practice arena
     */
    public static Optional<PracticeArena> getFreePracticeArena() {
        return getPracticeArenas().stream().filter(arena -> !PracticeMatchManager.getUsedPracticeArenasInPracticeMatches().contains(arena)).findFirst();
    }

    /**
     * Checks if there is a free practice arena
     *
     * @return if there is a freee practice arena
     */
    public static boolean isThereFreeArena() {
        return getFreePracticeArena().isEmpty();
    }

    /**
     * Loads all practice arenas
     */
    public static void loadPracticeArenasData() {
        YamlConfiguration configuration = LuxePracticeCore.getArenasSettings().getConfiguration();

        //Loads the practice arenas
        for (String arenaName : configuration.getKeys(false)) {

            //Gets practice arena spawns
            Location arenaSpawn1 = PracticeLocationUtil.getAsLocation(configuration.getString(arenaName + ".spawn1", "world:0:100:0:0:0"));
            Location arenaSpawn2 = PracticeLocationUtil.getAsLocation(configuration.getString(arenaName + ".spawn2", "world:0:100:0:0:0"));

            //Creates the practice arena
            createPracticeArena(arenaSpawn1, arenaSpawn2, arenaName);
        }
    }

    /**
     * Saves all practice arenas
     */
    public static void savePracticeArenasData() {
        YamlConfiguration configuration = LuxePracticeCore.getArenasSettings().getConfiguration();
        LuxePracticeCore.getArenasSettings().clear();

        //Saves the practice arenas
        for (PracticeArena arena : getPracticeArenas()) {
            configuration.set(arena.getName() + ".spawn1", PracticeLocationUtil.getAsString(arena.getSpawn1()));
            configuration.set(arena.getName() + ".spawn2", PracticeLocationUtil.getAsString(arena.getSpawn2()));
        }

        LuxePracticeCore.getArenasSettings().save();
    }

}