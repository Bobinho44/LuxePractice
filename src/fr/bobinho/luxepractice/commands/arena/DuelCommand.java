package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeDuelRequestManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("duel|1vs1|1v1")
public class DuelCommand extends BaseCommand {

    /**
     * Command duel (send)
     *
     * @param commandSender the sender
     * @param commandTarget the requested player
     */
    @Default
    @Syntax("/duel <player>")
    @CommandPermission("luxepractice.duel")
    public void onDuelSendCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender ->
                    PracticePlayerManager.getPracticePlayer(commandTarget.getPlayer().getUniqueId()).ifPresent(practiceReceiver -> {

                        //Checks if the practice sender is in a match
                        if (PracticeMatchManager.isInMatch(practiceSender)) {
                            commandSender.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                            return;
                        }

                        if (practiceReceiver.equals(practiceSender)) {
                            commandSender.sendMessage(ChatColor.RED + "You can't challenge yourself!");
                            return;
                        }

                        //Checks if the practice receiver is in a match
                        if (PracticeMatchManager.isInMatch(practiceReceiver)) {
                            commandSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " is already in an arena. He must leave it to look for a match!");
                            return;
                        }

                        //Checks if the practice sender has already sent a practice duel request
                        if (PracticeDuelRequestManager.isItPracticeDuelRequest(practiceSender, practiceReceiver)) {
                            commandSender.sendMessage(ChatColor.RED + "You have already sent a duel request to " + practiceReceiver.getName() + "!");
                            return;
                        }

                        //Sends a practice duel request
                        PracticeDuelRequestManager.sendPracticeDuelRequest(practiceSender, practiceReceiver);

                        //Sends the message to the practice sender
                        commandSender.sendMessage(ChatColor.GREEN + "You have sent a " + ChatColor.YELLOW + "1v1" + ChatColor.GREEN +
                                " request to " + ChatColor.YELLOW + practiceReceiver.getName() + ChatColor.GREEN + ".\n" +
                                ChatColor.GREEN + "This request will expire in " + ChatColor.YELLOW + "60" + ChatColor.GREEN + " seconds");

                        //Sends the message to the practice receiver
                        TextComponent request = new TextComponent(ChatColor.YELLOW + commandSender.getName() + ChatColor.GREEN + " has requested to " +
                                ChatColor.YELLOW + "1v1" + ChatColor.GREEN + " you in their kit.\nClick this message or type " +
                                ChatColor.YELLOW + "/1v1 accept " + commandSender.getName() + ChatColor.GREEN + " to accept.");
                        request.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/1vs1 accept " + commandSender.getName()));
                        practiceReceiver.sendMessage(request);
                    }));
        }
    }

    /**
     * Command duel (accept)
     *
     * @param commandSender the sender
     * @param commandTarget the target
     */
    @Subcommand("accept")
    @CommandPermission("luxepractice.duel")
    public void onDuelAcceptCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender ->
                    PracticePlayerManager.getPracticePlayer(commandTarget.getPlayer().getUniqueId()).ifPresent(practiceReceiver -> {

                        //Checks if the practice sender is in a match
                        if (PracticeMatchManager.isInMatch(practiceSender)) {
                            commandSender.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                            return;
                        }

                        //Checks if the practice receiver is in a match
                        if (PracticeMatchManager.isInMatch(practiceReceiver)) {
                            commandSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " is already in an arena. He must leave it to look for a match!");
                            return;
                        }

                        //Checks if the practice receiver has sent a practice duel request
                        if (!PracticeDuelRequestManager.isItPracticeDuelRequest(practiceReceiver, practiceSender)) {
                            commandSender.sendMessage(ChatColor.RED + "You did not receive a duel request from to " + practiceReceiver.getName() + "!");
                            return;
                        }

                        //Checks if there is a free arena
                        if (PracticeArenaManager.isThereFreeArena()) {
                            commandSender.sendMessage(ChatColor.RED + "No arena is available at the moment. Please try again later!");
                            return;
                        }

                        //Creates a new practice duel match
                        PracticeMatchManager.createPracticeDuelMatch(practiceReceiver, practiceSender);
                    }));
        }
    }

}