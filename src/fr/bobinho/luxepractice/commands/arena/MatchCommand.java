package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeWaitListManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("match|anon|anonymous")
public class MatchCommand extends BaseCommand {

    /**
     * Command match
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/match <kit>")
    @CommandPermission("luxepractice.match")
    public void onMatchCommand(CommandSender commandSender, @Optional String name) {
        if (commandSender instanceof Player) {

            //Checks if there is a basic practice kit
            if (PracticeKitManager.getDefaultBasicPracticeKit().isEmpty()) {
                ((Player) commandSender).sendMessage(ChatColor.RED + "There is no default kit!");
                return;
            }

            final String kitName = name != null ? name : PracticeKitManager.getDefaultBasicPracticeKit().get().getName();

            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                if (PracticeKitManager.getBasicPracticeKit(kitName).isEmpty()) {
                    practiceSender.sendMessage(ChatColor.RED + "The kit named " + kitName + " does not exist!");
                    return;
                }

                if (PracticeMatchManager.isInMatch(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                    return;
                }

                if (PracticeArenaManager.isThereFreeArena()) {
                    practiceSender.sendMessage(ChatColor.RED + "No arena is available at the moment. Please try again later!");
                    return;
                }

                if (PracticeWaitListManager.isAlreadyInThePracticeWaitList(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You are already on the waiting list for a match!");
                    return;
                }

                if (!PracticeWaitListManager.isThereAvailablePracticePlayer(kitName)) {
                    PracticeWaitListManager.addPracticePlayerToTheWaitList(practiceSender, kitName);
                    practiceSender.sendMessage(ChatColor.GOLD + "Searching for an opponent in kit +" + ChatColor.YELLOW + kitName);
                    return;
                }

                //Creates a new practice anonymous match
                PracticeMatchManager.createPracticeAnonymousMatch(practiceSender, kitName);
            });
        }
    }

}