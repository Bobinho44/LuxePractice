package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.request.PracticeTeamInviteRequestManager;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
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
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {
                PracticePlayerManager.getPracticePlayer(commandTarget.getPlayer().getUniqueId()).ifPresent(practiceReceiver -> {

                    //Checks if the practice sender has a practice team
                    if (!PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                        practiceSender.sendMessage(ChatColor.RED + "You doesn't have a team!");
                        return;
                    }

                    //Checks if the practice sender is the leader of his practice team
                    if (!PracticeTeamManager.isItPracticeTeamLeader(practiceSender)) {
                        practiceSender.sendMessage(ChatColor.RED + "You are not the leader of your team.");
                        return;
                    }

                    //Checks if the practice sender has already sent a practice team invite request
                    if (PracticeTeamInviteRequestManager.isItPracticeTeamInviteRequest(practiceSender, practiceReceiver)) {
                        practiceSender.sendMessage(ChatColor.RED + "You have already sent a request to " + practiceReceiver.getName() + "!");
                        return;
                    }

                    //Sends a practice team invite request
                    PracticeTeamInviteRequestManager.sendPracticeTeamInviteRequest(practiceSender, practiceReceiver);

                    //Messages
                    practiceSender.sendMessage(ChatColor.GREEN + "You have sent an invitation to join your team to " + practiceReceiver.getName() + ".");
                    TextComponent request = new TextComponent(ChatColor.GREEN + "You have received an invitation to join the " + practiceSender.getName() + " team." +
                            ChatColor.GREEN + "\nClick this message or type " +
                            ChatColor.YELLOW + "/teaminvite accept " + practiceSender.getName() + ChatColor.GREEN + " to accept.");
                    request.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teaminvite accept " + practiceSender.getName()));
                    practiceReceiver.sendMessage(request);
                });
            });
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
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {
                PracticePlayerManager.getPracticePlayer(commandTarget.getPlayer().getUniqueId()).ifPresent(practiceReceiver -> {

                    //Checks if the practice sender has a practice team
                    if (PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                        practiceSender.sendMessage(ChatColor.RED + "You already have a team!");
                        return;
                    }

                    //Checks if the practice sender has a practice team
                    if (!PracticeTeamManager.hasPracticeTeam(practiceReceiver)) {
                        practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " doesn't have a team.");
                        return;
                    }

                    //Checks if the practice sender have a practice team invite request
                    if (!PracticeTeamInviteRequestManager.isItPracticeTeamInviteRequest(practiceReceiver, practiceSender)) {
                        practiceSender.sendMessage(ChatColor.RED + "You doesn't have request from " + practiceReceiver.getName() + "!");
                        return;
                    }

                    //Gets the practice team
                    PracticeTeam practiceTeam = PracticeTeamManager.getPracticeTeam(practiceReceiver).get();

                    //Sends the message
                    practiceSender.sendMessage(ChatColor.GREEN + "You have join the " + practiceTeam.getLeader().getName() + "team.");
                    PracticeTeamManager.sendMessageToPracticeTeamMembers(practiceReceiver, ChatColor.GREEN + practiceSender.getName() + " joined the team.");

                    //Joins the practice team
                    PracticeTeamManager.joinPracticeTeam(practiceSender, practiceTeam);
                });
            });
        }
    }

}