package fr.bobinho.luxepractice.utils.kit;

import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PracticeKitManager {

    private static List<DefaultPracticeKit> defaultKits = new ArrayList<DefaultPracticeKit>();

    private static List<DefaultPracticeKit> getDefaultPracticetKits() {
        return defaultKits;
    }

    public static Optional<DefaultPracticeKit> getMainDefaultPracticeKit() {
        return getDefaultPracticetKits().stream().filter(kit -> kit.isMainDefaultKit()).findFirst();
    }

    public static boolean isAlreadyAnUsedDefaultPracticeKit(@Nonnull String defaultKitName) {
        Guards.checkNotNull(defaultKitName, "defaultKitName is null");

        return getDefaultPracticetKits().stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(defaultKitName));
    }

    public static Optional<DefaultPracticeKit> getDefaultPracticeKit(@Nonnull String defaultKitName) {
        Guards.checkNotNull(defaultKitName, "defaultKitName is null");

        return getDefaultPracticetKits().stream().filter(kit -> kit.getName().equalsIgnoreCase(defaultKitName)).findFirst();
    }

    public static void createDefaultPracticeKit(@Nonnull String defaultKitName, ItemStack[] items, boolean isDefaultMainKit) {
        Guards.checkNotNull(defaultKitName, "defaultKitName is null");
        Guards.checkNotNull(items, "items is null");
        Guards.checkArgument(!isAlreadyAnUsedDefaultPracticeKit(defaultKitName), "defaultKItName is already used");

        if (isDefaultMainKit) {
            getMainDefaultPracticeKit().ifPresent(kit -> kit.setMainDefaultKit(false));
        }

        if (getMainDefaultPracticeKit().isEmpty()) {
            isDefaultMainKit = true;
        }

        getDefaultPracticetKits().add(new DefaultPracticeKit(defaultKitName, items, isDefaultMainKit));
    }

    public static void deleteDefaultPracticeKit(@Nonnull String defaultKitName) {
        Guards.checkNotNull(defaultKitName, "defaultKitName is null");
        Guards.checkArgument(isAlreadyAnUsedDefaultPracticeKit(defaultKitName), "defaultKitName doesn't exist");

        getDefaultPracticetKits().add(getDefaultPracticeKit(defaultKitName).get());
    }

    public static void loadDefaultPracticeKits() {
        YamlConfiguration configuration = PracticeSettings.getConfiguration();

        //Loads default kits
        List<PracticeKit> kits = new ArrayList<PracticeKit>();
        if (configuration.getConfigurationSection("defaultKits") == null) {
            return;
        }

        for (String defaultKitName : configuration.getConfigurationSection("defaultKits").getKeys(false)) {

            //Loads default kit's items
            ItemStack[] items = new ItemStack[41];
            for (int i = 0; i < 41; i++) {
                items[i] = configuration.getItemStack("defaultKits." + defaultKitName + "." + i, null);
            }

            //Loads default kit main's statue
            boolean isMainDefaultKit = configuration.getBoolean("defaultKits." + defaultKitName + ".isMainDefaultKit", false);

            //Creates the default kit
            createDefaultPracticeKit(defaultKitName, items, isMainDefaultKit);
        }
    }

    public static void saveDefaultPracticeKits() {
        YamlConfiguration configuration = PracticeSettings.getConfiguration();

        //Saves default kits
        for (DefaultPracticeKit defaultKit : getDefaultPracticetKits()) {

            //Saves default kit's items
            for (int i = 0; i < 41; i++) {
                configuration.set("defaultKits." + defaultKit.getName() + "." + i, defaultKit.getItem(i));
            }

            //Saves default kit main's statue
            configuration.set("defaultKits." + defaultKit.getName() + ".isMainDefaultKit", defaultKit.isMainDefaultKit());
        }

        PracticeSettings.save();
    }

    public static boolean haveEmptyPracticeKitSlot(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return practicePlayer.getKits().size() < 10;
    }

    public static boolean isAlreadyAnUsedPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");

        return practicePlayer.getKits().stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(kitName));
    }

    public static void createPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(!isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "kitName is already used");
        Guards.checkArgument(haveEmptyPracticeKitSlot(practicePlayer), "practicePlayer have already 10 kits");

        practicePlayer.addKit(new PracticeKit(kitName, getPlayerInventoryAsKit(practicePlayer)));
    }

    public static void deletePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        PracticeKit kit = practicePlayer.getKit(kitName).get();
        if (practicePlayer.getAutoKit().equals(kit)) {
            removeAutoPracticeKit(practicePlayer);
        }
        practicePlayer.removeKit(kit);

    }

    public static ItemStack[] getPlayerInventoryAsKit(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        ItemStack[] items = new ItemStack[41];
        for (int i = 0; i < 41; i++) {
            items[i] = practicePlayer.getItem(i);
        }
        return items;
    }

    public static void givePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        for (int i = 0; i < 41; i++) {
            practicePlayer.setItem(i, practicePlayer.getKit(kitName).get().getItem(i));
        }
    }

    public static void givePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull PracticeKit practiceKit) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(practiceKit, "practiceKit is null");

        for (int i = 0; i < 41; i++) {
            practicePlayer.setItem(i, practiceKit.getItem(i));
        }
    }

    public static boolean haveAutoPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return practicePlayer.getAutoKit().isPresent();
    }

    public static void setAutoPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(!haveAutoPracticeKit(practicePlayer), "practicePlayer already have an auto kit");
        Guards.checkArgument(isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        practicePlayer.setAutoKit(practicePlayer.getKit(kitName).get());
    }

    public static void removeAutoPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(haveAutoPracticeKit(practicePlayer), "practicePlayer doesn't have an auto kit");

        practicePlayer.setAutoKit(null);
    }

    public static void giveSpectatorPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        for (int i = 0; i < 41; i++) {
            if (i == 0) practicePlayer.setItem(i, getInfo(PracticeMatchManager.getMatch(practicePlayer).get()));
            else if (i == 4) practicePlayer.setItem(i, getSpawnItem());
            else practicePlayer.setItem(i, null);
        }
        for (int j = 9; j < 36 && j < 9 + PracticeMatchManager.getMatch(practicePlayer).get().getFighters().size(); j++) {
            practicePlayer.setItem(j, getFighter(PracticeMatchManager.getMatch(practicePlayer).get().getFighters().get(j - 9)));
        }
    }

    private static ItemStack getInfo(PracticeMatch match) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(match.getMatchInfo());
        item.setItemMeta(meta);
        Bukkit.getConsoleSender().sendMessage("ZZZZZZZZZZZZZZZZZZZZZZZ");
        return item;
    }

    private static ItemStack getFighter(PracticePlayer practicePlayer) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta sm = (SkullMeta) skull.getItemMeta();
        sm.setDisplayName(ChatColor.GREEN + practicePlayer.getName());
        sm.setOwningPlayer(practicePlayer.getSpigotPlayer().get());
        skull.setItemMeta(sm);
        return  skull;
    }

    private static ItemStack getSpawnItem() {
        ItemStack item = new ItemStack(Material.OAK_DOOR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Back to the spawn");
        item.setItemMeta(meta);
        return item;
    }


}
