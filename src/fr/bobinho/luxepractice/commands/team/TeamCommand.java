package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("team")
public class TeamCommand extends BaseCommand {

    /**
     * Command team
     *
     * @param sender the sender
     */
    @Default
    @Syntax("/team")
    @CommandPermission("luxepractice.team")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            //Checks if the practice player has practice team
            if (!PracticeTeamManager.hasPracticeTeam(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You doesn't have a team!");
                return;
            }

            //Gets the practice team
            PracticeTeam practiceTeam = PracticeTeamManager.getPracticePlayerTeam(practicePlayer).get();

            //Gets practice team's members information
            StringBuilder practiceTeamMembers = new StringBuilder(ChatColor.GOLD + practiceTeam.getLeader().getName() + " team's members: ");
            for (PracticePlayer practiceMember : practiceTeam.getMembers()) {
                practiceTeamMembers.append(ChatColor.GOLD + "- " + ChatColor.YELLOW + practiceMember.getName());
            }

            //Sends message
            player.sendMessage(practiceTeamMembers.toString());

        }
    }

}