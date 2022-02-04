package fr.bobinho.luxepractice.utils.player;

import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

public class PracticePlayerManager {

    /**
     * The practice players list
     */
    private static final List<PracticePlayer> practicePlayers = new ArrayList<>();

    /**
     * Gets all practice players
     *
     * @return all practice players
     */
    @Nonnull
    private static List<PracticePlayer> getPracticePlayers() {
        return practicePlayers;
    }

    /**
     * Gets a practice player
     *
     * @param uuid the practice player uuid
     * @return the practice player
     */
    @Nonnull
    public static Optional<PracticePlayer> getPracticePlayer(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "uuid is null");

        return getPracticePlayers().stream().filter(practicePlayer -> practicePlayer.getUuid().equals(uuid)).findFirst();
    }

    /**
     * Checks if the uuid corresponds to a practice player
     *
     * @param uuid the practice player uuid
     * @return if it is a practice player
     */
    public static boolean isItPracticePlayer(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "uuid is null");

        return getPracticePlayer(uuid).isPresent();
    }

    /**
     * Registers a practice player
     *
     * @param uuid   the uuid
     * @param name   the name
     * @param kills  the kills number
     * @param deaths the deaths number
     * @return the practice player
     */
    @Nonnull
    public static PracticePlayer registerPracticePlayer(@Nonnull UUID uuid, @Nonnull String name, int kills, int deaths) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(name, "name is null");
        Validate.isTrue(!isItPracticePlayer(uuid), "player is already registered");

        PracticePlayer practicePlayer = new PracticePlayer(uuid, name, kills, deaths);
        getPracticePlayers().add(practicePlayer);
        return practicePlayer;
    }

    /**
     * Registers a practice player
     *
     * @param player the player
     */
    public static void registerPracticePlayer(@Nonnull Player player) {
        Validate.notNull(player, "player is null");
        Validate.isTrue(!isItPracticePlayer(player.getUniqueId()), "player is already registered");

        getPracticePlayers().add(new PracticePlayer(player));
    }

    /**
     * Unregisters a practice player
     *
     * @param practicePlayer the practice player
     */
    public static void unregisterPracticePlayer(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(isItPracticePlayer(practicePlayer.getUuid()), "practicePlayer is not registered");

        getPracticePlayers().remove(practicePlayer);
    }

    /**
     * Loads a practice player data
     *
     * @param player the player
     */
    public static void loadPracticePlayerData(@Nonnull Player player) {
        Validate.notNull(player, "player is null");
        Validate.isTrue(!isItPracticePlayer(player.getUniqueId()), "player is already registered");

        YamlConfiguration configuration = LuxePracticeCore.getPlayersSettings().getConfiguration();

        //Loads a new practice player
        if (configuration.getConfigurationSection(player.getUniqueId().toString()) == null) {
            registerPracticePlayer(player);
            return;
        }

        //Loads the practice player informations
        UUID uuid = player.getUniqueId();
        String name = configuration.getString(uuid + ".name", player.getName());

        //Loads the practice player stats
        int kills = configuration.getInt(uuid + ".stats.kills");
        int deaths = configuration.getInt(uuid + ".stats.deaths");

        //Registers the practice player
        PracticePlayer practicePlayer = registerPracticePlayer(uuid, name, kills, deaths);

        //Loads the practice player kits
        if (configuration.getConfigurationSection(uuid + ".kits") != null) {
            for (String kitName : Objects.requireNonNull(configuration.getConfigurationSection(uuid + ".kits")).getKeys(false)) {
                ItemStack[] kitItems = new ItemStack[41];
                for (int i = 0; i < 41; i++) {
                    kitItems[i] = configuration.getItemStack(uuid + ".kits." + kitName + "." + i);
                }
                PracticeKitManager.createPracticeKit(practicePlayer, kitName, kitItems);
            }
        }

        //Loads the practice player auto kit
        practicePlayer.setAutoKit(practicePlayer.getKit(configuration.getString(uuid + ".autokit", "")).orElse(null));

        //Loads the practice player old inventory
        ItemStack[] items = new ItemStack[41];
        for (int i = 0; i < 41; i++) {
            items[i] = configuration.getItemStack(uuid + ".oldinventory." + i);
        }

        practicePlayer.saveOldInventory(items);
    }

    /**
     * Saves the practice player data
     *
     * @param uuid the practice player uuid
     */
    public static void savePracticePlayerData(@Nonnull UUID uuid) {
        Validate.notNull(uuid, "uuid is null");
        Validate.isTrue(isItPracticePlayer(uuid), "player is not registered");

        YamlConfiguration configuration = LuxePracticeCore.getPlayersSettings().getConfiguration();
        configuration.set(uuid.toString(), null);

        getPracticePlayer(uuid).ifPresent(practicePlayer -> {

            //Saves practice player name
            configuration.set(uuid + ".name", practicePlayer.getName());

            //Saves player stats
            configuration.set(uuid + ".stats.kills", practicePlayer.getKills());
            configuration.set(uuid + ".stats.deaths", practicePlayer.getDeaths());

            //Saves practice player kits
            for (PracticeKit practiceKit : practicePlayer.getKits()) {
                for (int i = 0; i < 41; i++) {
                    configuration.set(uuid + ".kits." + practiceKit.getName() + "." + i, practiceKit.getItem(i));
                }
            }

            //Saves practice player autokit
            if (PracticeKitManager.haveAutoPracticeKit(practicePlayer)) {
                configuration.set(uuid + ".autokit", practicePlayer.getAutoKit().get().getName());
            }

            //Saves practice player old inventory
            for (int i = 0; i < 41; i++) {
                configuration.set(uuid + ".oldinventory." + i, practicePlayer.getOldInventory()[i]);
            }

            LuxePracticeCore.getPlayersSettings().save();
        });
    }

}
