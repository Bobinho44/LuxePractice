package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("delarena")
public class DelArenaCommand extends BaseCommand {

    /**
     * Command delarena
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/delarena <name>")
    @CommandPermission("luxepractice.delarena")
    public void onDelArenaCommand(CommandSender commandSender, @Single String arenaName) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if the arena name is already used
                if (!PracticeArenaManager.isItPracticeArena(arenaName)) {
                    practiceSender.sendMessage(ChatColor.RED + "There is no arena named " + arenaName + "!");
                    return;
                }

                //Deletes the arena
                PracticeArenaManager.deletePracticeArena(arenaName);

                //Sends the message
                practiceSender.sendMessage(ChatColor.GREEN + "You have deleted the " + arenaName + " arena.");
            });
        }
    }

}