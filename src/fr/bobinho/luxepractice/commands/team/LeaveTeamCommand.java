package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("leaveteam")
public class LeaveTeamCommand extends BaseCommand {

    /**
     * Command leaveteam
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/leaveteam")
    @CommandPermission("luxepractice.teaminvite")
    public void onLeaveTeamCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            PracticePlayer practiceSender = PracticePlayerManager.getPracticePlayer(sender.getUniqueId());

            //Checks if the practice sender has a practice team
            if (!PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                sender.sendMessage(ChatColor.RED + "You doesn't have a team!");
                return;
            }

            //Checks if the practice sender is the leader of his practice team
            if (PracticeTeamManager.isItPracticeTeamLeader(practiceSender)) {

                //Disbands the practice team
                PracticeTeamManager.sendMessageToPracticeTeamMembers(practiceSender, ChatColor.RED + "Your team has been disbanded!");
                PracticeTeamManager.deletePracticeTeam(practiceSender);
                return;
            }

            //Leaves the practice team
            PracticeTeamManager.leavePracticeTeam(practiceSender);

            //Messages
            sender.sendMessage(ChatColor.GREEN + "You have left your team.");
            PracticeTeamManager.sendMessageToPracticeTeamMembers(practiceSender, ChatColor.RED + sender.getName() + " left the team!");
        }
    }

}