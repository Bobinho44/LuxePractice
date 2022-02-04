package fr.bobinho.luxepractice.commands.inventory;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.inventory.PracticeInventoryHolder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
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
    public void onPracticeInventoryCommand(CommandSender commandSender, @Single OnlinePlayer streamer) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            //Checks if the uuid is valid
            if (streamer != null) {
                Inventory inventory = Bukkit.createInventory(new PracticeInventoryHolder(), InventoryType.PLAYER, Component.text(streamer.getPlayer().getName() + "'s inventory"));
                inventory.setContents(streamer.getPlayer().getInventory().getContents());
                player.openInventory(inventory);
            }
        }
    }

}