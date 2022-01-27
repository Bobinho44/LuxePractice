package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.arena.PracticeDuelRequest;
import fr.bobinho.luxepractice.utils.arena.match.DuelMatch;
import fr.bobinho.luxepractice.utils.arena.match.PracticeMatchManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import fr.bobinho.luxepractice.utils.scheduler.PracticeScheduler;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

@CommandAlias("duel|1vs1")
public class DuelCommand extends BaseCommand {

    /**
     * Command duel (send)
     *
     * @param sender   the sender
     * @param opponent the requested player
     */
    @Default
    @CommandPermission("luxepractice.duel")
    public void onDefault(CommandSender sender, @Single OnlinePlayer opponent) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());
            PracticePlayer practiceOpponent = PracticePlayerManager.getPracticePlayer(opponent.getPlayer().getUniqueId());

            if (PracticeMatchManager.isInMatch(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                return;
            }

            if (!PracticeArenaManager.isThereFreeArena()) {
                player.sendMessage(ChatColor.RED + "No arena is available at the moment. Please try again later!");
                return;
            }

            if (PracticeDuelRequest.hasSentPracticeDuelRequest(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You already have a duel request in progress!");
                return;
            }

            PracticeArena arena = PracticeArenaManager.getFreePracticeArena().get();
            PracticeKit kit = new PracticeKit(player.getName(), PracticeKitManager.getPlayerInventoryAsKit(practicePlayer));

            DuelMatch match = new DuelMatch(arena, practicePlayer, practiceOpponent, kit);

            player.sendMessage(ChatColor.GREEN + "You have sent a " + ChatColor.YELLOW + "1v1" + ChatColor.GREEN +
                    " request to " + ChatColor.YELLOW + opponent.getPlayer().getName() + ChatColor.GREEN + ".\n" +
                    ChatColor.GREEN + "This request will expire in " + ChatColor.YELLOW + "60" + ChatColor.GREEN + " seconds");

            TextComponent request = new TextComponent(ChatColor.YELLOW + player.getName() + ChatColor.GREEN + " has requested to " +
                    ChatColor.YELLOW + "1v1" + ChatColor.GREEN + " you in their kit.\nClick this message or type " +
                    ChatColor.YELLOW + "/1v1 accept <player>" + ChatColor.GREEN + " to accept.");
            request.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/1vs1 accept " + player.getName()));
            opponent.getPlayer().sendMessage(request);

            PracticeDuelRequest.sendPracticeDuelRequest(practicePlayer, practiceOpponent);

            PracticeScheduler.syncScheduler().after(60, TimeUnit.SECONDS).run(() -> {
                if (!match.isStarted()) {
                    PracticeDuelRequest.removePracticeDuelRequest(practicePlayer);
                }
            });
        }
    }

    /**
     * Command duel (accept)
     *
     * @param sender   the sender
     * @param opponent the requested player
     */
    @Subcommand("accept")
    @CommandPermission("luxepractice.duel")
    public void onAcceptRequest(CommandSender sender, @Single OnlinePlayer opponent) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());
            PracticePlayer practiceOpponent = PracticePlayerManager.getPracticePlayer(opponent.getPlayer().getUniqueId());

            if (PracticeMatchManager.isInMatch(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You are already in an arena. Leave it to look for a match!");
                return;
            }

            if (!PracticeArenaManager.isThereFreeArena()) {
                player.sendMessage(ChatColor.RED + "No arena is available at the moment. Please try again later!");
                return;
            }

            if (!PracticeDuelRequest.hasReceivedPracticeDuelRequest(practicePlayer, practiceOpponent)) {
                player.sendMessage(ChatColor.RED + "You have not received a duel request from " + opponent.getPlayer().getName() + "!");
                return;
            }

            PracticeMatchManager.getMatch(practiceOpponent).get().start();
        }
    }

}