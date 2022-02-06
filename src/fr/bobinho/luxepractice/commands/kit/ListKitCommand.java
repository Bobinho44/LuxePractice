package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("listkit")
public class ListKitCommand extends BaseCommand {

    /**
     * Command list kit
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/team")
    @CommandPermission("luxepractice.listkit")
    public void onListKitCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if the practice player has practice team
                if (practiceSender.getKits().size() == 0) {
                    practiceSender.sendMessage(ChatColor.RED + "You doesn't have a kit!");
                    return;
                }

                //Gets practice team's members information
                StringBuilder practiceSendersKits = new StringBuilder(ChatColor.GOLD + practiceSender.getName() + "'s kits: ");
                for (PracticeKit practiceKit : practiceSender.getKits()) {
                    practiceSendersKits.append(ChatColor.GOLD + "\n- " + ChatColor.YELLOW + practiceKit.getName());
                }

                //Sends message
                practiceSender.sendMessage(practiceSendersKits.toString());
            });
        }
    }

}