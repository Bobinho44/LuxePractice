package fr.bobinho.luxepractice.commands.chest;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;

import fr.bobinho.luxepractice.utils.chest.PracticeChestManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("delchest")
public class DelChestCommand extends BaseCommand {

        @Default
        @CommandPermission("luxepractice.delchest")
        public void onDefault(CommandSender sender) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                Block targetedBlock = player.getTargetBlock(5);

                if (targetedBlock != null && targetedBlock.getType() == Material.CHEST) {
                    if (!PracticeChestManager.isItPracticeChest(targetedBlock.getLocation())) {
                        player.sendMessage(ChatColor.RED + "This chest is not infinite!");
                    } else {
                        PracticeChestManager.deletePracticeChest(targetedBlock.getLocation());
                        player.sendMessage(ChatColor.GREEN + "This chest is now not infinite!");
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "You don't target any chests!");
                }
            }
        }

    }

