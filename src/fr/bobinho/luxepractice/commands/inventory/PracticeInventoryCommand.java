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

import java.util.UUID;

@CommandAlias("practiceinventory")
public class PracticeInventoryCommand extends BaseCommand {

    /**
     * Command practiceinventory
     *
     * @param sender the sender
     */
    @Default
    @CommandPermission("luxepractice.practiceinventory")
    public void onDefault(CommandSender sender, @Single OnlinePlayer streamer) {
        if (sender instanceof Player) {

            Bukkit.getConsoleSender().sendMessage("TESTINV " + streamer.getPlayer().getName());
            //Checks if the uuid is valid
            if (streamer != null) {
                Bukkit.getConsoleSender().sendMessage("TESTINV2");
                Bukkit.createInventory(new PracticeInventoryHolder(), InventoryType.PLAYER, Component.text(streamer.getPlayer().getName() + "'s inventory"));
                ((Player) sender).openInventory(streamer.getPlayer().getInventory());
            }
        }
    }

}

