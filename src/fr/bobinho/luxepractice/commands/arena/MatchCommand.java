package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.PracticeWaitList;
import fr.bobinho.luxepractice.utils.arena.match.AnonymousMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
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
    public void onDefault(CommandSender sender, @Optional String kitName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            if (kitName == null && PracticeKitManager.getMainDefaultPracticeKit().isPresent()) {
                player.sendMessage(ChatColor.RED + "The kit named " + kitName + " does not exist!");
                return;
            }

            if (PracticeMatchManager.isInMatch(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                return;
            }

            if (!PracticeArenaManager.isThereFreeArena()) {
                player.sendMessage(ChatColor.RED + "No arena is available at the moment. Please try again later!");
                return;
            }

            if (PracticeWaitList.isAlreadyInThePracticeWaitList(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You are already on the waiting list for a match!");
                return;
            }

            if (!PracticeWaitList.isThereAvailablePracticePlayer()) {
                player.sendMessage(ChatColor.GOLD + "Searching for an opponent in kit + " + kitName);
                return;
            }

            PracticePlayer practiceOpponent = PracticeWaitList.getPracticePlayerFromTheWaitList().get();
            PracticeArena arena = PracticeArenaManager.getFreePracticeArena().get();
            PracticeKit kit = kitName == null ? PracticeKitManager.getMainDefaultPracticeKit().get() : PracticeKitManager.getDefaultPracticeKit(kitName).get();

            AnonymousMatch match = new AnonymousMatch(arena, practicePlayer, practiceOpponent, kit);

            match.start();
        }
    }

}
