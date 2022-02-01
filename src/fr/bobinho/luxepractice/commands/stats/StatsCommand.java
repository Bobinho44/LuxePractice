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
     * @param sender the sender
     */
    @Default
    @Syntax("/stats")
    @CommandPermission("luxepractice.stats")
    public void onDefault(CommandSender sender, @Optional OnlinePlayer target) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(target == null ? player.getUniqueId() : target.player.getUniqueId());

            //Gets player's stats
            String stats = ChatColor.GOLD + sender.getName() + "'s stats:" + "\n" +
                    ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + practicePlayer.getKills() + "\n" +
                    ChatColor.GOLD + "Deaths: " + ChatColor.YELLOW + practicePlayer.getDeaths() + "\n" +
                    ChatColor.GOLD + "KDR: " + ChatColor.YELLOW + practicePlayer.getRatio() + "\n";

            //Sends player's stats
            player.sendMessage(stats);
        }
    }

}
