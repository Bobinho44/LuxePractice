package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.arena.request.PracticeTeamDuelRequestManager;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("teamduel")
public class TeamDuelCommand extends BaseCommand {

    /**
     * Command teamduel (send)
     *
     * @param commandSender the sender
     * @param commandTarget the requested player
     */
    @Default
    @Syntax("/teamduel <team's leader>")
    @CommandPermission("luxepractice.teamduel")
    public void onDuelSendCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender ->
                    PracticePlayerManager.getPracticePlayer(commandTarget.getPlayer().getUniqueId()).ifPresent(practiceReceiver -> {

                        //Checks if the practice sender is in a match
                        if (PracticeMatchManager.isInMatch(practiceSender)) {
                            practiceSender.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                            return;
                        }

                        if (practiceReceiver.equals(practiceSender)) {
                            practiceSender.sendMessage(ChatColor.RED + "You can't challenge your team!");
                            return;
                        }

                        //Checks if the practice receiver is in a match
                        if (PracticeMatchManager.isInMatch(practiceReceiver)) {
                            practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " is already in an arena. He must leave it to look for a match!");
                            return;
                        }

                        //Checks if the practice sender has a practice team
                        if (!PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                            practiceSender.sendMessage(ChatColor.RED + "You do not have a team!");
                            return;
                        }

                        //Checks if the practice sender has a practice team
                        if (!PracticeTeamManager.hasPracticeTeam(practiceReceiver)) {
                            practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " doesn't have a team!");
                            return;
                        }

                        if (!PracticeTeamManager.isItPracticeTeamLeader(practiceReceiver)) {
                            practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " is not the leader of his team!");
                            return;
                        }

                        //Checks if the practice receiver has a practice team
                        if (!PracticeTeamManager.hasPracticeTeam(practiceReceiver)) {
                            practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " has no team!");
                            return;
                        }

                        //Checks if a practice team member is in a match
                        if (PracticeMatchManager.practiceTeamMemberIsInMatch(practiceSender)) {
                            practiceSender.sendMessage(ChatColor.RED + "A member of your team is already in a match!");
                            return;
                        }

                        //Checks if the practice sender has already sent a practice team duel request
                        if (PracticeTeamDuelRequestManager.isItPracticeTeamDuelRequest(practiceSender, practiceReceiver)) {
                            practiceSender.sendMessage(ChatColor.RED + "You have already sent a duel request to " + practiceReceiver.getName() + "!");
                            return;
                        }

                        //Sends a practice team duel request
                        PracticeTeamDuelRequestManager.sendPracticeTeamDuelRequest(practiceSender, practiceReceiver);

                        //Sends the message to the practice sender
                        practiceSender.sendMessage(ChatColor.GREEN + "You have sent a " + ChatColor.YELLOW + "team fight" + ChatColor.GREEN +
                                " request to " + ChatColor.YELLOW + practiceReceiver.getName() + ChatColor.GREEN + "'s team.\n" +
                                ChatColor.GREEN + "This request will expire in " + ChatColor.YELLOW + "60" + ChatColor.GREEN + " seconds");

                        //Sends the message to the practice receiver
                        TextComponent request = new TextComponent(ChatColor.YELLOW + practiceSender.getName() + ChatColor.GREEN + " has requested to " +
                                ChatColor.YELLOW + "team fight" + ChatColor.GREEN + " your team.\nClick this message or type " +
                                ChatColor.YELLOW + "/teamduel accept " + practiceSender.getName() + ChatColor.GREEN + " to accept.");
                        request.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/teamduel accept " + commandSender.getName()));
                        practiceReceiver.sendMessage(request);
                    }));
        }
    }

    /**
     * Command teamduel (accept)
     *
     * @param commandSender the sender
     * @param commandTarget the target
     */
    @Subcommand("accept")
    @Syntax("/teamduel accept <team's leader>")
    @CommandPermission("luxepractice.teamduel")
    public void onDuelAcceptCommand(CommandSender commandSender, @Single OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender ->
                    PracticePlayerManager.getPracticePlayer(commandTarget.getPlayer().getUniqueId()).ifPresent(practiceReceiver -> {

                        //Checks if the practice sender is in a match
                        if (PracticeMatchManager.isInMatch(practiceSender)) {
                            practiceSender.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                            return;
                        }

                        //Checks if the practice receiver is in a match
                        if (PracticeMatchManager.isInMatch(practiceReceiver)) {
                            practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " is already in an arena. He must leave it to look for a match!");
                            return;
                        }

                        //Checks if the practice sender has a practice team
                        if (!PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                            practiceSender.sendMessage(ChatColor.RED + "You do not have a team!");
                            return;
                        }

                        //Checks if the practice receiver has a practice team
                        if (!PracticeTeamManager.hasPracticeTeam(practiceReceiver)) {
                            practiceSender.sendMessage(ChatColor.RED + practiceReceiver.getName() + " has no team!");
                            return;
                        }

                        if (!PracticeTeamManager.isItPracticeTeamLeader(practiceSender)) {
                            commandSender.sendMessage(ChatColor.RED + "You are not the leader of your team!");
                            return;
                        }

                        //Checks if a practice team member is in a match
                        if (PracticeMatchManager.practiceTeamMemberIsInMatch(practiceSender)) {
                            commandSender.sendMessage(ChatColor.RED + "A member of your team is already in a match!");
                            return;
                        }

                        //Checks if the practice receiver has sent a practice team duel request
                        if (!PracticeTeamDuelRequestManager.isItPracticeTeamDuelRequest(practiceReceiver, practiceSender)) {
                            commandSender.sendMessage(ChatColor.RED + "You did not receive a duel request from to " + practiceReceiver.getName() + "!");
                            return;
                        }

                        //Checks if there is a free arena
                        if (PracticeArenaManager.isThereFreeArena()) {
                            commandSender.sendMessage(ChatColor.RED + "No arena is available at the moment. Please try again later!");
                            return;
                        }

                        //Creates a new team duel practice match
                        PracticeMatchManager.createPracticeTeamDuelMatch(practiceReceiver, practiceSender);
                    }));
        }
    }

}