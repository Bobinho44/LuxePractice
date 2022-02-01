package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setkit")
public class SetKitCommand extends BaseCommand {

    /**
     * Command setkit
     *
     * @param sender  the sender
     * @param defaultKitName the kit name
     * @param isMainDefaultKit the kit main statue
     */
    @Default
    @Syntax("/setkit <name> <isMainDefault = true|false>")
    @CommandPermission("luxepractice.setkit")
    public void onDefault(CommandSender sender, @Single String defaultKitName, @Single String isMainDefaultKit) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PracticePlayer practicePlayer = PracticePlayerManager.getPracticePlayer(player.getUniqueId());

            //Checks if  the default kit name is already use
            if (PracticeKitManager.isAlreadyAnUsedDefaultPracticeKit(defaultKitName)) {
                player.sendMessage(ChatColor.RED + "There is already a default kit named " + defaultKitName + "!");
                return;
            }
Boolean.parseBoolean("");
            //Creates the default kit
            PracticeKitManager.createDefaultPracticeKit(defaultKitName, PracticeKitManager.getPlayerInventoryAsKit(practicePlayer), Boolean.parseBoolean(isMainDefaultKit));
            player.sendMessage(ChatColor.GREEN + "You have created the default kit " + defaultKitName + ".");
        }
    }

}