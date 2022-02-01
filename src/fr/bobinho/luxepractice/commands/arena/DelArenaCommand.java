package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("delarena")
public class DelArenaCommand extends BaseCommand {

    /**
     * Command delarena
     *
     * @param sender the sender
     */
    @Default
    @Syntax("/delarena <name>")
    @CommandPermission("luxepractice.delarena")
    public void onDefault(CommandSender sender, @Single String arenaName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Checks if the arena name is already used
            if (!PracticeArenaManager.isItPracticeArena(arenaName)) {
                player.sendMessage(ChatColor.RED + "There is no arena named " + arenaName + "!");
                return;
            }

            //Deletes the arena
            PracticeArenaManager.deletePracticeArena(arenaName);

            //Sends the message
            player.sendMessage(ChatColor.GREEN + "You have deleted the " + arenaName + " arena.");
        }
    }

}