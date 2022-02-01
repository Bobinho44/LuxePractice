package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeDuelRequestManager;
import fr.bobinho.luxepractice.utils.arena.match.DuelMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
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
            Player sender = (Player) commandSender;
            Player receiver = commandTarget.getPlayer();
            PracticePlayer practiceSender = PracticePlayerManager.getPracticePlayer(sender.getUniqueId());
            PracticePlayer practiceReceiver = PracticePlayerManager.getPracticePlayer(receiver.getUniqueId());

            //Checks if the practice sender is in a match
            if (PracticeMatchManager.isInMatch(practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                return;
            }

            if (practiceReceiver.equals(practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You can't challenge yourself!");
                return;
            }

            //Checks if the practice receiver is in a match
            if (PracticeMatchManager.isInMatch(practiceReceiver)) {
                sender.sendMessage(ChatColor.RED + receiver.getName() + " is already in an arena. He must leave it to look for a match!");
                return;
            }

            //Checks if the practice sender has already sent a practice duel request
            if (PracticeDuelRequestManager.isItPracticeDuelRequest(practiceSender, practiceReceiver)) {
                sender.sendMessage(ChatColor.RED + "You have already sent a duel request to " + receiver.getName() + "!");
                return;
            }

            //Sends a practice duel request
            PracticeDuelRequestManager.sendPracticeDuelRequest(practiceSender, practiceReceiver);

            //Messages
            sender.sendMessage(ChatColor.GREEN + "You have sent a " + ChatColor.YELLOW + "1v1" + ChatColor.GREEN +
                    " request to " + ChatColor.YELLOW + receiver.getName() + ChatColor.GREEN + ".\n" +
                    ChatColor.GREEN + "This request will expire in " + ChatColor.YELLOW + "60" + ChatColor.GREEN + " seconds");

            TextComponent request = new TextComponent(ChatColor.YELLOW + sender.getName() + ChatColor.GREEN + " has requested to " +
                    ChatColor.YELLOW + "1v1" + ChatColor.GREEN + " you in their kit.\nClick this message or type " +
                    ChatColor.YELLOW + "/1v1 accept " + sender.getName() + ChatColor.GREEN + " to accept.");
            request.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/1vs1 accept " + sender.getName()));
            receiver.sendMessage(request);
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
            Player sender = (Player) commandSender;
            Player receiver = commandTarget.getPlayer();
            PracticePlayer practiceSender = PracticePlayerManager.getPracticePlayer(sender.getUniqueId());
            PracticePlayer practiceReceiver = PracticePlayerManager.getPracticePlayer(receiver.getUniqueId());

            //Checks if the practice sender is in a match
            if (PracticeMatchManager.isInMatch(practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                return;
            }

            //Checks if the practice receiver is in a match
            if (PracticeMatchManager.isInMatch(practiceReceiver)) {
                sender.sendMessage(ChatColor.RED + receiver.getName() + " is already in an arena. He must leave it to look for a match!");
                return;
            }

            //Checks if the practice receiver has sent a practice duel request
            if (!PracticeDuelRequestManager.isItPracticeDuelRequest(practiceReceiver, practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You did not receive a duel request from to " + receiver.getName() + "!");
                return;
            }

            //Checks if there is a free arena
            if (!PracticeArenaManager.isThereFreeArena()) {
                sender.sendMessage(ChatColor.RED + "No arena is available at the moment. Please try again later!");
                return;
            }

            //Gets practice duel informations
            PracticeArena arena = PracticeArenaManager.getFreePracticeArena().get();
            PracticeKit kit = new PracticeKit(receiver.getName(), PracticeKitManager.getPlayerInventoryAsKit(practiceReceiver));

            //Starts the practice duel
            new DuelMatch(arena, practiceReceiver, practiceSender, kit).start();
        }
    }

}