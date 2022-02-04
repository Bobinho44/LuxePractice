package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setarena")
public class SetArenaCommand extends BaseCommand {

    /**
     * Command setarena
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/setarena <name>")
    @CommandPermission("luxepractice.setarena")
    public void onSetArenaCommand(CommandSender commandSender, @Single String arenaName) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if the arena name is already used
                if (PracticeArenaManager.isItPracticeArena(arenaName)) {
                    practiceSender.sendMessage(ChatColor.RED + "There is already an arena named " + arenaName + "!");
                    return;
                }

                //Creates the arena
                PracticeArenaManager.createPracticeArena(practiceSender.getLocation(), arenaName);

                //Sends the message
                practiceSender.sendMessage(ChatColor.GREEN + "You have set the " + arenaName + " arena.");
            });
        }
    }

}