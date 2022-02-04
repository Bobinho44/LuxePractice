package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("createteam")
public class CreateTeamCommand extends BaseCommand {

    /**
     * Command createteam
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/createteam")
    @CommandPermission("luxepractice.createteam")
    public void onCreateTeamCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                if (PracticeTeamManager.hasPracticeTeam(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You already have a team!");
                    return;
                }

                PracticeTeamManager.createPracticeTeam(practiceSender);
                practiceSender.sendMessage(ChatColor.GREEN + "You have created your team.");
            });
        }
    }

}