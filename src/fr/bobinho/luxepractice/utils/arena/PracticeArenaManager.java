package fr.bobinho.luxepractice.utils.arena;

import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.settings.PracticeArenas;
import fr.bobinho.luxepractice.utils.settings.PracticePlayers;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class PracticeArenaManager {

    private static final List<PracticeArena> arenas = new ArrayList<PracticeArena>();

    private static List<PracticeArena> getArenas() {
        return arenas;
    }

    public static void createPracticeArena(@Nonnull Location spawn, @Nonnull String name) {
        Guards.checkNotNull(spawn, "spawn is null");
        Guards.checkNotNull(name, "name is null");

        arenas.add(new PracticeArena(spawn, name));
    }

    private static Optional<PracticeArena> getArena(@Nonnull String arenaName) {
        Guards.checkNotNull(arenaName, "name is null");

        return getArenas().stream().filter(arena -> arena.getName().equalsIgnoreCase(arenaName)).findFirst();
    }

    public static void deletePracticeArena(@Nonnull String arenaName) {
        Guards.checkNotNull(arenaName, "name is null");
        Guards.checkArgument(isItPracticeArena(arenaName), "arenaName is not valid");

        arenas.remove(getArena(arenaName).get());
    }

    public static boolean isItPracticeArena(@Nonnull String arenaName) {
        return getArena(arenaName).isPresent();
    }

    public static Optional<PracticeArena> getFreePracticeArena() {
        return getArenas().stream().filter(arena -> !PracticeMatchManager.getUsedPracticeArenas().contains(arena)).findFirst();
    }

    public static boolean isThereFreeArena() {
        return getFreePracticeArena().isPresent();
    }

    public static void loadPracticeArenasData() {
        YamlConfiguration configuration = PracticePlayers.getConfiguration();
            for (String arenaName : configuration.getKeys(false)) {

                Location spawn = PracticeLocationUtil.getAsLocation(configuration.getString(arenaName + ".spawn"));

                createPracticeArena(spawn, arenaName);
        }
    }

    public static void savePracticePlayerData(@Nonnull UUID uuid) {

        YamlConfiguration configuration = PracticeArenas.getConfiguration();
        PracticeArenas.clear();

        for (PracticeArena arena : getArenas()) {
            configuration.set(arena.getName() + ".spawn", PracticeLocationUtil.getAsString(arena.getSpawn()));
        }
    }

}
