package fr.bobinho.luxepractice.commands.chest;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.chest.PracticeChestManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setchest")
public class SetChestCommand extends BaseCommand {

    /**
     * Command setchest
     *
     * @param sender the sender
     */
    @Default
    @Syntax("/setchest")
    @CommandPermission("luxepractice.setchest")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Block targetedBlock = player.getTargetBlock(5);

            //Checks if player target a chest
            if (targetedBlock == null || targetedBlock.getType() != Material.CHEST) {
                player.sendMessage(ChatColor.RED + "You don't target any chests!");
                return;
            }

            //Checks is the chest is a practice chest
            if (PracticeChestManager.isItPracticeChest(targetedBlock.getLocation())) {
                player.sendMessage(ChatColor.RED + "This chest is already infinite!");
                return;
            }

            //Creates the practice chest
            PracticeChestManager.createPracticeChest(targetedBlock.getLocation());
            player.sendMessage(ChatColor.GREEN + "This chest is now infinite!");

        }
    }

}
