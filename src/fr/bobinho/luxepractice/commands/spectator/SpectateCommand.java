package fr.bobinho.luxepractice.commands.spectator;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("spectate|spec")
public class SpectateCommand extends BaseCommand {

    /**
     * Command spectate
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/spectate <player>")
    @CommandPermission("luxepractice.spectate")
    public void onSpectateCommand(CommandSender commandSender, @Single OnlinePlayer player) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender ->
                    PracticePlayerManager.getPracticePlayer(player.getPlayer().getUniqueId()).ifPresent(practiceReceiver -> {

                //Checks if the selected player is in an arena
                if (!PracticeMatchManager.isInMatch(practiceReceiver)) {
                    practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " is not in an arena!");
                    return;
                }

                //Checks if the selected player is in an arena
                if (PracticeMatchManager.isInMatch(practiceSender)) {
                    PracticeMatchManager.getPracticeMatch(practiceSender).get().removeSpectator(practiceSender);
                }

                //Creates the arena
                PracticeMatchManager.addSpectator(practiceSender, practiceReceiver);

                //Sends the message
                practiceSender.sendMessage(ChatColor.YELLOW + "Now spectating " + ChatColor.GRAY + practiceReceiver.getName() + "'s " + ChatColor.YELLOW + "match...");
            }));
        }
    }

}