package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
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
     * @param sender the sender
     */
    @Default
    @CommandPermission("luxepractice.disbandteam")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            if (!PracticeTeamManager.hasPracticeTeam(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You doesn't have a team!");
                return;
            }

            if (!PracticeTeamManager.getPracticePlayerTeam(practicePlayer).get().getLeader().equals(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You are not the leader of your team!");
                return;
            }

            PracticeTeamManager.deletePracticeTeam(practicePlayer);
            player.sendMessage(ChatColor.GREEN + "You have deleted your team.");

        }
    }

}