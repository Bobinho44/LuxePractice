package fr.bobinho.luxepractice.utils.chest;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PracticeChestManager {

    /**
     * Fields
     */
    private static final List<Location> practiceChests = new ArrayList<>();

    /**
     * Gets all practice chests
     *
     * @return all practice chests
     */
    @Nonnull
    private static List<Location> getPracticeChests() {
        return practiceChests;
    }

    /**
     * Checks if the location correspond to a practice chest location
     *
     * @param location the location
     * @return if it is a practice chest location
     */
    public static boolean isItPracticeChest(@Nonnull Location location) {
        Objects.requireNonNull(location, "location is null");

        return getPracticeChests().stream().anyMatch(chestLocation -> chestLocation.equals(location));
    }

    /**
     * Creates a new practice chest
     * @param location the practice chest location
     */
    public static void createPracticeChest(@Nonnull Location location) {
        Objects.requireNonNull(location, "location is null");
        Guards.checkArgument(!isItPracticeChest(location), "chest is already a practice chest");

        getPracticeChests().add(location);
    }

    /**
     * Deletes the practice chest
     * @param location the practice chest location
     */
    public static void deletePracticeChest(@Nonnull Location location) {
        Objects.requireNonNull(location, "location is null");
        Guards.checkArgument(isItPracticeChest(location), "chest is not a practice chest");

        getPracticeChests().remove(location);
    }

    /**
     * Opens a practice chest to the practice player
     * @param practicePlayer the practice player
     * @param chest the practice chest
     */
    public static void openPracticeChest(@Nonnull PracticePlayer practicePlayer, @Nonnull Chest chest) {
        Objects.requireNonNull(practicePlayer, "practicePlayer is null");
        Objects.requireNonNull(chest, "chest is null");
        Guards.checkArgument(isItPracticeChest(chest.getLocation()), "chest is not a practice chest");

        Inventory practiceChestInventory = Bukkit.createInventory(null, chest.getInventory().getSize());

        //Adds item to the practice chest
        for (int i = 0; i < practiceChestInventory.getSize(); i++) {
            practiceChestInventory.setItem(i, chest.getInventory().getItem(i) != null ? chest.getInventory().getItem(i).clone() : null);
        }

        //Opens the practice chest
        practicePlayer.openInventory(practiceChestInventory);
    }

}
