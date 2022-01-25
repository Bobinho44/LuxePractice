package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.match.AnonymousMatch;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("match|anon|anonymous")
public class MatchCommand extends BaseCommand {

    /**
     * Command match
     *
     * @param sender the sender
     */
    @Default
    @CommandPermission("luxepractice.match")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());
            player.sendMessage(practicePlayer.getName() + " " + practicePlayer.getKills());
            PracticeArena arena = new PracticeArena(player.getLocation());
            AnonymousMatch match = new AnonymousMatch(arena, practicePlayer, practicePlayer);
            player.sendMessage(match.getStartMessage(practicePlayer));
            match.setWinner(practicePlayer);
            player.sendMessage("AAAAAAAAAAAAAAAAAAAAAAA\n");
            player.sendMessage(match.getEndMessage(practicePlayer));
        }
    }

}
