package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("autokit")
public class AutoKitCommand extends BaseCommand {

    /**
     * Command autokit set
     *
     * @param commandSender the sender
     * @param kitName       the kit name
     */
    @Subcommand("set")
    @Syntax("/setautokit <name>")
    @CommandPermission("luxepractice.setautokit")
    public void onAutoKitSetCommand(CommandSender commandSender, @Single String kitName) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if practice player have an auto kit
                if (PracticeKitManager.haveAutoPracticeKit(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You already have an auto kit!");
                    return;
                }

                //Checks if practice kit name is already use
                if (!PracticeKitManager.isItPracticeKit(practiceSender, kitName)) {
                    practiceSender.sendMessage(ChatColor.RED + "You don't have a kit named " + kitName + "!");
                    return;
                }

                //Sets the practice auto kit
                PracticeKitManager.setAutoPracticeKit(practiceSender, kitName);
                PracticeKitManager.givePracticeKit(practiceSender, kitName);
                practiceSender.sendMessage(ChatColor.GREEN + "You have defined the kit " + kitName + " as the auto kit.");
            });
        }
    }

    /**
     * Command autokit remove
     *
     * @param commandSender the sender
     */
    @Subcommand("remove")
    @CommandPermission("luxepractice.removeautokit")
    public void onAutoKitRemoveCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if practice player have an auto kit
                if (!PracticeKitManager.haveAutoPracticeKit(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You don't have an auto kit!");
                    return;
                }

                //Removes the practice auto kit
                PracticeKitManager.removeAutoPracticeKit(practiceSender);
                practiceSender.sendMessage(ChatColor.GREEN + "You have removed your auto kit.");
            });
        }
    }

}