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

@CommandAlias("savekit")
public class SaveKitCommand extends BaseCommand {

    /**
     * Command savekit
     *
     * @param sender  the sender
     * @param kitName the kit name
     */
    @Default
    @CommandPermission("luxepractice.savekit")
    public void onDefault(CommandSender sender, @Single String kitName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            //Checks if kit name is already use
            if (PracticeKitManager.isAlreadyAnUsedPracticeKit(practicePlayer, kitName)) {
                player.sendMessage(ChatColor.RED + "You already have a kit named " + kitName + "!");
                return;
            }

            //Checks if the player have less than 10 kit
            if (PracticeKitManager.haveEmptyPracticeKitSlot(practicePlayer)) {
                player.sendMessage(ChatColor.RED + "You already have 10 kits!");
                return;
            }

            //Creates the kit
            PracticeKitManager.createPracticeKit(practicePlayer, kitName);
            player.sendMessage(ChatColor.GREEN + "You have created the kit " + kitName + ".");
        }
    }

}
