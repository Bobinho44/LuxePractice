package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setkit")
public class UnsetKitCommand extends BaseCommand {

    /**
     * Command setkit
     *
     * @param commandSender  the sender
     * @param defaultKitName the kit name
     */
    @Default
    @Syntax("/unsetkit <name>")
    @CommandPermission("luxepractice.unsetkit")
    public void onUnsetKitCommand(CommandSender commandSender, @Single String defaultKitName) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if the default practice kit name exist
                if (!PracticeKitManager.isItBasicPracticeKit(defaultKitName)) {
                    practiceSender.sendMessage(ChatColor.RED + "There is no default kit named " + defaultKitName + "!");
                    return;
                }

                //Deletes the default practice kit
                PracticeKitManager.deleteBasicPracticeKit(defaultKitName);
                practiceSender.sendMessage(ChatColor.GREEN + "You have deleted the default kit " + defaultKitName + ".");
            });
        }
    }

}