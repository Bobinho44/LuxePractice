package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("loadkit")
public class LoadKitCommand extends BaseCommand {

    /**
     * Command load kit
     *
     * @param commandSender the sender
     * @param kitName       the kit name
     */
    @Default
    @Syntax("/loadkit <name>")
    @CommandPermission("luxepractice.loadkit")
    public void onLoadKitCommand(CommandSender commandSender, @Single String kitName) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if kit name is already use
                if (!PracticeKitManager.isItPracticeKit(practiceSender, kitName)) {
                    practiceSender.sendMessage(ChatColor.RED + "You don't have a kit named " + kitName + "!");
                    return;
                }

                //Gives the kit
                PracticeKitManager.givePracticeKit(practiceSender, kitName);
                practiceSender.sendMessage(ChatColor.GREEN + "You have received the kit " + kitName + ".");
            });
        }
    }

}