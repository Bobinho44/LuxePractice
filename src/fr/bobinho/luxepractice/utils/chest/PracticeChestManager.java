package fr.bobinho.luxepractice.utils.chest;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PracticeChestManager {

    private static final List<Location> practiceChests = new ArrayList<Location>();

    private static List<Location> getPracticeChests() {
        return practiceChests;
    }

    public static boolean isItPracticeChest(Location location) {
        return getPracticeChests().stream().anyMatch(chestLocation -> chestLocation.equals(location));
    }

    public static Inventory openPracticeChest(Player player, Chest chest) {
        Guards.checkArgument(isItPracticeChest(chest.getLocation()), "chest is not a practice chest");

        Inventory practiceChestInventory = Bukkit.createInventory(null, chest.getInventory().getSize());

        for(int i = 0; i < practiceChestInventory.getSize(); i++) {
            practiceChestInventory.setItem(i, chest.getInventory().getItem(i).clone());
        }

        return practiceChestInventory;
    }

    public static void createPracticeChest(Location location) {
        Guards.checkArgument(!isItPracticeChest(location), "chest is already a practice chest");

        getPracticeChests().add(location);
    }

    public static void deletePracticeChest(Location location) {
        Guards.checkArgument(isItPracticeChest(location), "chest is not a practice chest");

        getPracticeChests().remove(location);
    }

}
