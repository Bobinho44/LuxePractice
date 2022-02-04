package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("delkit")
public class DelKitCommand extends BaseCommand {

    /**
     * Command delkit
     *
     * @param commandSender the sender
     * @param kitName       the kit name
     */
    @Default
    @Syntax("/delkit <name>")
    @CommandPermission("luxepractice.delkit")
    public void onDelKitCommand(CommandSender commandSender, @Single String kitName) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if practice kit name is already use
                if (!PracticeKitManager.isItPracticeKit(practiceSender, kitName)) {
                    practiceSender.sendMessage(ChatColor.RED + "You don't have a kit named " + kitName + "!");
                    return;
                }

                //Deletes the practice kit
                PracticeKitManager.deletePracticeKit(practiceSender, kitName);
                practiceSender.sendMessage(ChatColor.GREEN + "You have deleted the kit " + kitName + ".");
            });
        }
    }

}