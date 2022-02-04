package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("disbandteam")
public class DisbandTeamCommand extends BaseCommand {

    /**
     * Command disbandteam
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/disbandteam")
    @CommandPermission("luxepractice.disbandteam")
    public void onDisbandTeamCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if the practice player has practice team
                if (!PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You doesn't have a team!");
                    return;
                }

                //Checks if the practice player is a leader of his practice team
                if (!PracticeTeamManager.isItPracticeTeamLeader(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You are not the leader of your team!");
                    return;
                }

                //Deletes a practice team
                PracticeTeamManager.deletePracticeTeam(practiceSender);
                practiceSender.sendMessage(ChatColor.GREEN + "You have deleted your team.");
            });
        }
    }

}