package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("loadkit")
public class LoadKitCommand extends BaseCommand {

    /**
     * Command load kit
     *
     * @param sender  the sender
     * @param kitName the kit name
     */
    @Default
    @CommandPermission("luxepractice.loadkit")
    public void onDefault(CommandSender sender, @Single String kitName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            //Checks if kit name is already use
            if (!PracticeKitManager.isAlreadyAnUsedPracticeKit(practicePlayer, kitName)) {
                player.sendMessage(ChatColor.RED + "You don't have a kit named " + kitName + "!");
                return;
            }

            //Gives the kit
            PracticeKitManager.givePracticeKit(practicePlayer, kitName);
            player.sendMessage(ChatColor.GREEN + "You have received the kit " + kitName + ".");
        }
    }

}
