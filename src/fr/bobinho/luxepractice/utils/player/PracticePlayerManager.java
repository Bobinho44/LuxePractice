package fr.bobinho.luxepractice.utils.player;

import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.settings.PracticePlayers;
import org.atlanmod.commons.Guards;
import org.atlanmod.commons.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PracticePlayerManager {

    private static final List<PracticePlayer> practicePlayers = new ArrayList<PracticePlayer>();

    private static List<PracticePlayer> getPracticePlayers() {
        return practicePlayers;
    }

    private static boolean isAlreadyRegistered(@Nonnull UUID uuid) {
        Preconditions.requireThat(uuid).isNotNull();

        return getPracticePlayers().stream().anyMatch(practicePlayer -> practicePlayer.getUuid().equals(uuid));
    }


    public static PracticePlayer getPracticePlayer(@Nonnull UUID uuid) {
        Guards.checkNotNull(uuid, "uuid is null");
        Guards.checkArgument(isAlreadyRegistered(uuid), "player is not registered");

        return getPracticePlayers().stream().filter(practicePlayer -> practicePlayer.getUuid().equals(uuid)).findFirst().get();
    }

    private static void addPlayer(@Nonnull PracticePlayer practicePlayer) {
        Preconditions.requireThat(practicePlayer).isNotNull();
        Preconditions.requireThat(isAlreadyRegistered(practicePlayer.getUuid())).isFalse();

        getPracticePlayers().add(practicePlayer);
    }

    private static void removePlayer(@Nonnull PracticePlayer practicePlayer) {
        Preconditions.requireThat(practicePlayer).isNotNull();
        Preconditions.requireThat(isAlreadyRegistered(practicePlayer.getUuid())).isTrue();

        getPracticePlayers().remove(practicePlayer);
    }

    public static void loadPracticePlayerData(@Nonnull UUID uuid) {
        Guards.checkNotNull(uuid, "uuid is null");
        Guards.checkArgument(!isAlreadyRegistered(uuid), "the player is already registered");
Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + " AAAAAAAAAAAAAAAAAAAAAAAAA ");
        YamlConfiguration configuration = PracticePlayers.getConfiguration();

        //Loads player's name
        String name = configuration.getString(uuid + ".name");

        //Loads player's stats
        int kills = configuration.getInt(uuid + ".stats.kills");
        int deaths = configuration.getInt(uuid + ".stats.deaths");

        //Loads player's kits
        List<PracticeKit> kits = new ArrayList<PracticeKit>();
        if (configuration.getConfigurationSection(uuid + ".kits") != null) {
            for (String kitName : configuration.getConfigurationSection(uuid + ".kits").getKeys(false)) {
                ItemStack[] items = new ItemStack[36];
                for (int i = 0; i < 36; i++) {
                    items[i] = configuration.getItemStack(uuid + ".kits." + kitName + "." + i);
                }
                kits.add(new PracticeKit(kitName, items));
            }
        }

        //Loads player's auto kit
        PracticeKit autokit = kits.stream()
                .filter(kit -> kit.getName().equals(configuration.getString(uuid + ".autokit")))
                .findFirst().orElse(null);

        //Creates practice player
        addPlayer(name == null ? new PracticePlayer(Bukkit.getPlayer(uuid)) : new PracticePlayer(uuid, name, kills, deaths, kits, autokit));
    }

    public static void savePracticePlayerData(@Nonnull UUID uuid) {
        Guards.checkNotNull(uuid, "uuid is null");
        Guards.checkArgument(isAlreadyRegistered(uuid), "the player is not registered");

        YamlConfiguration configuration = PracticePlayers.getConfiguration();
        PracticePlayer practicePlayer = getPracticePlayer(uuid);

        //Saves player's name
        configuration.set(uuid + ".name", practicePlayer.getName());

        //Saves player's stats
        configuration.set(uuid + ".stats.kills", practicePlayer.getKits());
        configuration.set(uuid + ".stats.deaths", practicePlayer.getDeaths());

        //Saves player's kits
        for (PracticeKit kits : practicePlayer.getKits()) {
            for (int i = 0; i < 36; i++) {
                configuration.set(uuid + ".kits." + kits.getName() + "." + i, kits.getItem(i));
            }
        }

        //Saves player's autokit
        if (PracticeKitManager.haveAutoPracticeKit(practicePlayer)) {
            configuration.set(uuid + ".autokit", practicePlayer.getAutoKit().get().getName());
        }
    }

}
