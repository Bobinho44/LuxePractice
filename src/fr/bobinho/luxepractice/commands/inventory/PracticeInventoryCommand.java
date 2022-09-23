package fr.bobinho.luxepractice.commands.inventory;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.inventory.PracticeInventoryHolder;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

@CommandAlias("practiceinventory")
public class PracticeInventoryCommand extends BaseCommand {

    /**
     * Command practiceinventory
     *
     * @param commandSender the sender
     */
    @Default
    @CommandPermission("luxepractice.practiceinventory")
    public void onPracticeInventoryCommand(CommandSender commandSender, @Single String key, @Single String uuid, @Single String arena) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (!key.equals("Mm7kTCD2")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
                return;
            }

            //Gets practice match
            PracticeMatch practiceMatch = PracticeMatchManager.getPracticeMatch(PracticeArenaManager.getPracticeArena(arena).get()).get();

            //Creates the inventory
            Inventory inventory = Bukkit.createInventory(new PracticeInventoryHolder(), InventoryType.PLAYER, Component.text(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName() + "'s inventory"));

            for (int i = 0; i < 36; i++) {
                inventory.setItem(i, practiceMatch.getFighterInventory(UUID.fromString(uuid)).get()[i]);
            }

            //Opens the inventory
            player.openInventory(inventory);
        }
    }

}
