package fr.bobinho.luxepractice.utils.item;

import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PracticeItemUtils {

    /**
     * Gets the practice spectator inventory information item
     * @param practiceMatch the practice match
     * @return the practice spectator inventory information item
     */
    public static ItemStack getPracticeSpectatorInventoryInfoItem(PracticeMatch practiceMatch) {
        return new PracticeItemBuilder(Material.BOOK).name(practiceMatch.getMatchInfoAsString()).build();
    }

    /**
     * Gets the practice spectator inventory spawn item
     * @return the practice spectator inventory spawn item
     */
    public static ItemStack getPracticeSpectatorInventorySpawnItem() {
        return new PracticeItemBuilder(Material.OAK_DOOR).name(ChatColor.GOLD + "Back to the spawn").build();
    }

    /**
     * Gets the practice spectator inventory fighter item
     * @param practicePlayer the practice player
     * @return the practice spectator inventory fighter item
     */
    public static ItemStack getPracticeSpectatorInventoryFighterItem(PracticePlayer practicePlayer) {
        return new PracticeItemBuilder(practicePlayer.getSpigotPlayer()).name(ChatColor.GREEN + practicePlayer.getName())
                .lore(practicePlayer.getHealthInformation())
                .lore(practicePlayer.getActivePotionEffectsInformation())
                .build();
    }

}
