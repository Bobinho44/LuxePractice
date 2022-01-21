package fr.bobinho.luxepractice.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("stats")
public class StatsCommand extends BaseCommand {

    @Default
    @CommandPermission("luxepractice.stats")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPlayer(player);

            String stats = ChatColor.GOLD + sender.getName() + "'s stats:\n" +
                    ChatColor.GOLD + "Kills: " + ChatColor.YELLOW + practicePlayer.getKills() +
                    ChatColor.GOLD + "Deaths: " + ChatColor.YELLOW + practicePlayer.getDeaths() +
                    ChatColor.GOLD + "KDR: " + ChatColor.YELLOW + practicePlayer.getRatio();

            player.sendMessage(stats);
        }
    }

}
