package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("createteam")
public class CreateTeamCommand extends BaseCommand {

    /**
     * Command createteam
     *
     * @param sender the sender
     */
    @Default
    @Syntax("/createteam")
    @CommandPermission("luxepractice.createteam")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            if (PracticeTeamManager.hasPracticeTeam(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You already have a team!");
                return;
            }

            PracticeTeamManager.createPracticeTeam(practicePlayer);
            player.sendMessage(ChatColor.GREEN + "You have created your team.");

        }
    }

}