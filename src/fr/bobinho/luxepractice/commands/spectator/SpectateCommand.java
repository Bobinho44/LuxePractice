package fr.bobinho.luxepractice.commands.spectator;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("spectate|spec")
public class SpectateCommand extends BaseCommand {

    /**
     * Command spectate
     *
     * @param sender the sender
     */
    @Default
    @CommandPermission("luxepractice.spectate")
    public void onDefault(CommandSender sender, @Single OnlinePlayer player) {
        if (sender instanceof Player) {
            Player viewer = (Player) sender;
            PracticePlayer practiceViewer = PracticePlayerManager.getPracticePlayer(viewer.getUniqueId());
            PracticePlayer practiceStreamer = PracticePlayerManager.getPracticePlayer(player.getPlayer().getUniqueId());

            //Checks if the selected player is in an arena
            if (!PracticeMatchManager.isInMatch(practiceStreamer)) {
                viewer.sendMessage(ChatColor.RED + practiceStreamer.getSpigotPlayer().get().getName() + "is not in an arena!");
                return;
            }

            //Checks if the selected player is in an arena
            if (PracticeMatchManager.isInMatch(practiceViewer)) {
                viewer.sendMessage(ChatColor.RED + "You are already in an arena!");
                return;
            }

            //Creates the arena
            PracticeMatchManager.addSpectator(practiceViewer, practiceStreamer);
        }
    }

}