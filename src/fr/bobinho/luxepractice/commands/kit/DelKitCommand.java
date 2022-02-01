package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("delkit")
public class DelKitCommand extends BaseCommand {

    /**
     * Command delkit
     *
     * @param sender  the sender
     * @param kitName the kit name
     */
    @Default
    @Syntax("/delkit <name>")
    @CommandPermission("luxepractice.delkit")
    public void onDefault(CommandSender sender, @Single String kitName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            //Checks if kit name is already use
            if (!PracticeKitManager.isAlreadyAnUsedPracticeKit(practicePlayer, kitName)) {
                player.sendMessage(ChatColor.RED + "You don't have a kit named " + kitName + "!");
                return;
            }

            //Deletes the kit
            PracticeKitManager.deletePracticeKit(practicePlayer, kitName);
            player.sendMessage(ChatColor.GREEN + "You have deleted the kit " + kitName + ".");
        }
    }

}
