package fr.bobinho.luxepractice.commands.stats;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("stats")
public class StatsCommand extends BaseCommand {

    /**
     * Command stats
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/stats")
    @CommandPermission("luxepractice.stats")
    public void onStatsCommand(CommandSender commandSender, @Optional OnlinePlayer commandTarget) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer((commandTarget == null ? ((Player) commandSender): commandTarget.getPlayer()).getUniqueId()).ifPresent(practicePlayer -> {

                //Gets the target practice player stats
                String stats = ChatColor.GOLD + practicePlayer.getName() + "'s stats:" + "\n" +
                        ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + practicePlayer.getKills() + "\n" +
                        ChatColor.GOLD + "Deaths: " + ChatColor.YELLOW + practicePlayer.getDeaths() + "\n" +
                        ChatColor.GOLD + "KDR: " + ChatColor.YELLOW + practicePlayer.getRatio() + "\n";

                //Sends the target practice player stats
                commandSender.sendMessage(stats);
            });
        }
    }

}