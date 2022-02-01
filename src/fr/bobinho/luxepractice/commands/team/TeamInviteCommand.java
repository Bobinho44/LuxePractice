package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.request.PracticeTeamInviteRequestManager;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("teaminvite")
public class TeamInviteCommand extends BaseCommand {

    /**
     * Command teaminvite (send)
     *
     * @param commandSender the sender
     * @param commandTarget the target
     */
    @Default
    @Syntax("/teaminvite <player>")
    @CommandPermission("luxepractice.teaminvite")
    public void onTeamInviteCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            Player receiver = commandTarget.getPlayer();
            PracticePlayer practiceSender = PracticePlayerManager.getPracticePlayer(sender.getUniqueId());
            PracticePlayer practiceReceiver = PracticePlayerManager.getPracticePlayer(receiver.getUniqueId());

            //Checks if the practice sender has a practice team
            if (!PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You doesn't have a team!");
                return;
            }

            //Checks if the practice sender is the leader of his practice team
            if (!PracticeTeamManager.isItPracticeTeamLeader(practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You are not the leader of your team.");
                return;
            }

            //Checks if the practice sender has already sent a practice team invite request
            if (PracticeTeamInviteRequestManager.isItPracticeTeamInviteRequest(practiceSender, practiceReceiver)) {
                sender.sendMessage(ChatColor.RED + "You have already sent a request to " + receiver.getName() + "!");
                return;
            }

            //Sends a practice team invite request
            PracticeTeamInviteRequestManager.sendPracticeTeamInviteRequest(practiceSender, practiceReceiver);

            //Messages
            sender.sendMessage(ChatColor.GREEN + "You have sent an invitation to join your team to " + receiver.getName() + ".");
            TextComponent request = new TextComponent(ChatColor.GREEN + "You have received an invitation to join the " + sender.getName() + " team." +
                    ChatColor.GREEN + "\nClick this message or type " +
                    ChatColor.YELLOW + "/teamjoin " + sender.getName() + ChatColor.GREEN + " to accept.");
            request.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teamjoin " + sender.getName()));
            receiver.sendMessage(request);
        }
    }

    /**
     * Command teaminvite (accept)
     *
     * @param commandSender the sender
     * @param commandTarget the target
     */
    @Subcommand("accept")
    @CommandPermission("luxepractice.teaminvite")
    public void onTeamInviteAcceptCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            Player receiver = commandTarget.getPlayer();
            PracticePlayer practiceSender = PracticePlayerManager.getPracticePlayer(sender.getUniqueId());
            PracticePlayer practiceReceiver = PracticePlayerManager.getPracticePlayer(receiver.getUniqueId());

            //Checks if the practice sender has a practice team
            if (PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You already have a team!");
                return;
            }

            //Checks if the practice sender has a practice team
            if (!PracticeTeamManager.hasPracticeTeam(practiceReceiver)) {
                sender.sendMessage(ChatColor.RED + receiver.getName() + " doesn't have a team.");
                return;
            }

            //Checks if the practice sender have a practice team invite request
            if (PracticeTeamInviteRequestManager.isItPracticeTeamInviteRequest(practiceReceiver, practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You doesn't have request from " + receiver.getName() + "!");
                return;
            }

            //Gets the practice team
            PracticeTeam practiceTeam = PracticeTeamManager.getPracticePlayerTeam(practiceReceiver).get();

            //Messages
            sender.sendMessage(ChatColor.GREEN + "You have join the " + practiceTeam.getLeader().getName() + "team.");
            PracticeTeamManager.sendMessageToPracticeTeamMembers(practiceSender, ChatColor.GREEN + sender.getName() + " joined the team.");

            practiceTeam.addMember(practiceSender);
        }
    }

}