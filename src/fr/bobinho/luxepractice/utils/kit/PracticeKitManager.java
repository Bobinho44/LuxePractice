package fr.bobinho.luxepractice.utils.kit;

import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.item.PracticeItemUtils;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.apache.commons.lang.Validate;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PracticeKitManager {

    /**
     * The basic practice kits list
     */
    private static final List<BasicPracticeKit> basicPracticeKits = new ArrayList<>();

    /**
     * Gets all basic practice kits
     *
     * @return all basic practice kits
     */
    @Nonnull
    private static List<BasicPracticeKit> getBasicPracticeKits() {
        return basicPracticeKits;
    }

    /**
     * Gets a basic practice kit
     *
     * @param basicKitName the basic practice kit name
     * @return the basic practice kit
     */
    @Nonnull
    public static Optional<BasicPracticeKit> getBasicPracticeKit(@Nonnull String basicKitName) {
        Validate.notNull(basicKitName, "basicKitName is null");

        return getBasicPracticeKits().stream().filter(kit -> kit.getName().equalsIgnoreCase(basicKitName)).findFirst();
    }

    /**
     * Gets the default basic practice kit
     *
     * @return the default basic practice kit
     */
    @Nonnull
    public static Optional<BasicPracticeKit> getDefaultBasicPracticeKit() {
        return getBasicPracticeKits().stream().filter(BasicPracticeKit::isDefaultKit).findFirst();
    }

    /**
     * Checks if the basic kit name is associated with a basic practice kit
     *
     * @param basicKitName the basic practice kit name
     * @return if it is a basic practice kit
     */
    public static boolean isItBasicPracticeKit(@Nonnull String basicKitName) {
        Validate.notNull(basicKitName, "basicKitName is null");

        return getBasicPracticeKit(basicKitName).isPresent();
    }

    /**
     * Creates a new basic practice kit
     *
     * @param basicKitName      the basic practice kit name
     * @param basicKitItems     the basic practice kit items
     * @param isDefaultBasicKit if the basic practice kit is the new default basic practice kit
     */
    public static void createBasicPracticeKit(@Nonnull String basicKitName, ItemStack[] basicKitItems, boolean isDefaultBasicKit) {
        Validate.notNull(basicKitName, "basicKitName is null");
        Validate.notNull(basicKitItems, "basicKitItems is null");
        Validate.isTrue(!isItBasicPracticeKit(basicKitName), "defaultKItName is already used");

        //Checks if there is already a default basic practice kit
        if (isDefaultBasicKit) {
            getDefaultBasicPracticeKit().ifPresent(kit -> kit.setIsDefaultKit(false));
        }

        //Checks if there is no default basic practice kit
        if (getDefaultBasicPracticeKit().isEmpty()) {
            isDefaultBasicKit = true;
        }

        //Creates a new basic practice kit
        getBasicPracticeKits().add(new BasicPracticeKit(basicKitName, basicKitItems, isDefaultBasicKit));
    }

    /**
     * Deletes a basic practice kit
     *
     * @param basicKitName the basic practice kit name
     */
    public static void deleteBasicPracticeKit(@Nonnull String basicKitName) {
        Validate.notNull(basicKitName, "basicKitName is null");
        Validate.isTrue(isItBasicPracticeKit(basicKitName), "basicKitName is invalid");

        getBasicPracticeKits().remove(getBasicPracticeKit(basicKitName).get());
    }

    /**
     * Loads all basic practice kits
     */
    public static void loadBasicPracticeKits() {
        YamlConfiguration configuration = LuxePracticeCore.getMainSettings().getConfiguration();

        //Loads basic practice kits
        if (configuration.getConfigurationSection("basicKits") == null) {
            return;
        }

        if (configuration.getConfigurationSection("basicKits") != null) {
            for (String defaultKitName : Objects.requireNonNull(configuration.getConfigurationSection("basicKits")).getKeys(false)) {

                //Loads the basic practice kits items
                ItemStack[] basicKitItems = new ItemStack[41];
                for (int i = 0; i < 41; i++) {
                    basicKitItems[i] = configuration.getItemStack("basicKits." + defaultKitName + "." + i, null);
                }

                //Loads the basic practice kits default statue
                boolean isDefaultBasicKit = configuration.getBoolean("basicKits." + defaultKitName + ".isDefaultBasicKit", false);

                //Creates the basic practice kits
                createBasicPracticeKit(defaultKitName, basicKitItems, isDefaultBasicKit);
            }
        }
    }

    public static void saveBasicPracticeKits() {
        YamlConfiguration configuration = LuxePracticeCore.getMainSettings().getConfiguration();
        configuration.set("basicKits", null);

        //Saves the basic practice kits
        for (BasicPracticeKit defaultKit : getBasicPracticeKits()) {

            //Saves the basic practice kits items
            for (int i = 0; i < 41; i++) {
                configuration.set("basicKits." + defaultKit.getName() + "." + i, defaultKit.getItem(i));
            }

            //Saves the basic practice kits default statue
            configuration.set("basicKits." + defaultKit.getName() + ".isDefaultBasicKit", defaultKit.isDefaultKit());
        }

        LuxePracticeCore.getMainSettings().save();
    }

    /**
     * Checks if the practice player has empty practice kit slot
     *
     * @param practicePlayer the practice player
     * @return is there is empty practice kit slot
     */
    public static boolean haveEmptyPracticeKitSlot(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        return practicePlayer.getKits().size() < 10;
    }

    /**
     * Checks if the kit name is associated with a practice kit
     *
     * @param practicePlayer the practice player
     * @param kitName        the practice kit name
     * @return if it is a practice kit
     */
    public static boolean isItPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(kitName, "kitName is null");

        return practicePlayer.getKits().stream().anyMatch(kit -> kit.getName().equalsIgnoreCase(kitName));
    }

    /**
     * Creates a new practice kit
     *
     * @param practicePlayer the practice player
     * @param kitName        the practice kit name
     */
    public static void createPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(kitName, "kitName is null");
        Validate.isTrue(!isItPracticeKit(practicePlayer, kitName), "kitName is already used");
        Validate.isTrue(haveEmptyPracticeKitSlot(practicePlayer), "practicePlayer have already 10 kits");

        practicePlayer.addKit(new PracticeKit(kitName, getPracticePlayerInventoryAsKit(practicePlayer)));
    }

    /**
     * Creates a new practice kit
     *
     * @param practicePlayer the practice player
     * @param kitName        the practice kit name
     * @param kitItems       the practice kit items
     */
    public static void createPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName, @Nonnull ItemStack[] kitItems) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(kitName, "kitName is null");
        Validate.isTrue(!isItPracticeKit(practicePlayer, kitName), "kitName is already used");
        Validate.isTrue(haveEmptyPracticeKitSlot(practicePlayer), "practicePlayer have already 10 kits");

        practicePlayer.addKit(new PracticeKit(kitName, kitItems));
    }

    /**
     * Deletes the practice kit
     *
     * @param practicePlayer the practice player
     * @param kitName        the practice kit name
     */
    public static void deletePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(kitName, "kitName is null");
        Validate.isTrue(isItPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        //Gets the practice kit
        PracticeKit kit = practicePlayer.getKit(kitName).get();

        //Checks if the practice kit is the auto practice kit of the practice player
        practicePlayer.getAutoKit().ifPresent(autoKit -> {
            if (autoKit.equals(kit)) {
                removeAutoPracticeKit(practicePlayer);
            }
        });

        //Deletes the practice kit
        practicePlayer.removeKit(kit);
    }

    /**
     * Gets the practice player inventory as kit
     *
     * @param practicePlayer the practice player
     * @return the practice player inventory as kit
     */
    @Nonnull
    public static ItemStack[] getPracticePlayerInventoryAsKit(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        //Gets the practice player inventory items
        ItemStack[] kitItems = new ItemStack[41];
        for (int i = 0; i < 41; i++) {
            kitItems[i] = practicePlayer.getItem(i);
        }
        return kitItems;
    }

    /**
     * Gives a practice kit to the practice player
     *
     * @param practicePlayer the practice player
     * @param kitName        the practice kit name
     */
    public static void givePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(kitName, "kitName is null");
        Validate.isTrue(isItPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        //Gives all practice kit items
        for (int i = 0; i < 41; i++) {
            practicePlayer.setItem(i, practicePlayer.getKit(kitName).get().getItem(i));
        }
    }

    /**
     * Gives a practice kit to the practice player
     *
     * @param practicePlayer the practice player
     * @param practiceKit    the practice kit
     */
    public static void givePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull PracticeKit practiceKit) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(practiceKit, "practiceKit is null");

        //Gives all practice kit items
        for (int i = 0; i < 41; i++) {
            practicePlayer.setItem(i, practiceKit.getItem(i));
        }
    }

    /**
     * Checks if a practice player has an practice auto kit
     *
     * @param practicePlayer the practice player
     * @return if he has a practice auto kit
     */
    public static boolean haveAutoPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");

        return practicePlayer.getAutoKit().isPresent();
    }

    /**
     * Sets the auto practice kit
     *
     * @param practicePlayer the practice player
     * @param autoKitName    the practice auto kit name
     */
    public static void setAutoPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String autoKitName) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.notNull(autoKitName, "autoKitName is null");
        Validate.isTrue(!haveAutoPracticeKit(practicePlayer), "practicePlayer already have an auto kit");
        Validate.isTrue(isItPracticeKit(practicePlayer, autoKitName), "this kit doesn't exist");

        practicePlayer.setAutoKit(practicePlayer.getKit(autoKitName).get());
    }

    /**
     * Removes the auto practice kit
     *
     * @param practicePlayer the practice player
     */
    public static void removeAutoPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(haveAutoPracticeKit(practicePlayer), "practicePlayer doesn't have an auto kit");

        practicePlayer.setAutoKit(null);
    }

    /**
     * Gives a spectator practice kit to the practice player
     *
     * @param practicePlayer the practice player
     */
    public static void giveSpectatorPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Validate.notNull(practicePlayer, "practicePlayer is null");
        Validate.isTrue(PracticeMatchManager.isInMatch(practicePlayer), "practicePlayer is not in a match");

        PracticeMatch practiceMatch = PracticeMatchManager.getPracticeMatch(practicePlayer).get();

        //Adds item to the practice player inventory
        for (int i = 0; i < 41; i++) {

            //Adds the information item
            if (i == 0) {
                practicePlayer.setItem(i, PracticeItemUtils.getPracticeSpectatorInventoryInfoItem(practiceMatch));
            }

            //Adds back to the spawn item
            else if (i == 4) {
                practicePlayer.setItem(i, PracticeItemUtils.getPracticeSpectatorInventorySpawnItem());
            }

            //Clears inventory
            else {
                practicePlayer.setItem(i, null);
            }
        }

        //Adds fighters information item
        for (int j = 9; j < 36 && j < 9 + practiceMatch.getFighters().size(); j++) {
            practicePlayer.setItem(j, PracticeItemUtils.getPracticeSpectatorInventoryFighterItem(practiceMatch.getFighters().get(j - 9)));
        }
    }

}
