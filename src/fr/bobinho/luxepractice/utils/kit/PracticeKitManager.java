package fr.bobinho.luxepractice.utils.kit;

import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.atlanmod.commons.Guards;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PracticeKitManager {

    public static boolean isItPracticeKit(Player player, String kitName) {
        return PracticePlayerManager.getPlayer(player).isItPracticeKit(kitName);
    }

    public static ItemStack[] getPlayerInventoryAsKit(Player player) {
        ItemStack[] items = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            items[i] = player.getInventory().getItem(i).clone();
        }
        return items;
    }

    public static void givePracticeKit(Player player, String kitName) {
        Guards.checkArgument(isItPracticeKit(player, kitName));

        PracticeKit kit = PracticePlayerManager.getPlayer(player).getKit(kitName).get();
        for (int i = 0; i < 36; i++) {
            player.getInventory().setItem(i, kit.getItem(i));
        }
    }


}
