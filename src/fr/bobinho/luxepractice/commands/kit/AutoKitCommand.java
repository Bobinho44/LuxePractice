package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Single;
import co.aikar.commands.annotation.Subcommand;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("autokit")
public class AutoKitCommand extends BaseCommand {

    /**
     * Command autokit set
     *
     * @param sender  the sender
     * @param kitName the kit name
     */
    @Subcommand("set")
    @CommandPermission("luxepractice.setautokit")
    public void onSet(CommandSender sender, @Single String kitName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            //Checks if player have an auto kit
            if (PracticeKitManager.haveAutoPracticeKit(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You already have an auto kit!");
                return;
            }

            //Checks if kit name is already use
            if (!PracticeKitManager.isAlreadyAnUsedPracticeKit(practicePlayer, kitName)) {
                player.sendMessage(ChatColor.RED + "You don't have a kit named " + kitName + "!");
                return;
            }

            //Sets the auto kit
            PracticeKitManager.setAutoPracticeKit(practicePlayer, kitName);
            PracticeKitManager.givePracticeKit(practicePlayer, kitName);
            player.sendMessage(ChatColor.GREEN + "You have defined the kit " + kitName + " as the auto kit.");
        }
    }

    /**
     * Command autokit remove
     *
     * @param sender the sender
     */
    @Subcommand("remove")
    @CommandPermission("luxepractice.removeautokit")
    public void onRemove(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            //Checks if player have an auto kit
            if (!PracticeKitManager.haveAutoPracticeKit(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You don't have an auto kit!");
                return;
            }

            //Removes the auto kit
            PracticeKitManager.removeAutoPracticeKit(practicePlayer);
            player.sendMessage(ChatColor.GREEN + "You have removed your auto kit.");
        }
    }

}
