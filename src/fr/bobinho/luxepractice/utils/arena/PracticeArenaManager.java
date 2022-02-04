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
     * @param arenaSpawn the practice arena spawn
     * @param arenaName  the practice arena name
     */
    public static void createPracticeArena(@Nonnull Location arenaSpawn, @Nonnull String arenaName) {
        Validate.notNull(arenaSpawn, "arenaSpawn is null");
        Validate.notNull(arenaName, "arenaName is null");

        getPracticeArenas().add(new PracticeArena(arenaSpawn, arenaName));
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

            //Creates the practice arena
            createPracticeArena(PracticeLocationUtil.getAsLocation(configuration.getString(arenaName + ".spawn", "world:0:0:0:0:0")), arenaName);
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
            configuration.set(arena.getName() + ".spawn", PracticeLocationUtil.getAsString(arena.getSpawn()));
        }

        LuxePracticeCore.getArenasSettings().save();
    }

}