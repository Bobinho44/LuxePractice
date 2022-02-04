package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("savekit")
public class SaveKitCommand extends BaseCommand {

    /**
     * Command savekit
     *
     * @param commandSender the sender
     * @param kitName       the kit name
     */
    @Default
    @Syntax("/savekit <name>")
    @CommandPermission("luxepractice.savekit")
    public void onSaveKitCommand(CommandSender commandSender, @Single String kitName) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if practice kit name is already use
                if (PracticeKitManager.isItPracticeKit(practiceSender, kitName)) {
                    practiceSender.sendMessage(ChatColor.RED + "You already have a kit named " + kitName + "!");
                    return;
                }

                //Checks if the practice player have less than 10 kit
                if (!PracticeKitManager.haveEmptyPracticeKitSlot(practiceSender)) {
                    practiceSender.sendMessage(ChatColor.RED + "You already have 10 kits!");
                    return;
                }

                //Creates the practice kit
                PracticeKitManager.createPracticeKit(practiceSender, kitName);
                practiceSender.sendMessage(ChatColor.GREEN + "You have created the kit " + kitName + ".");
            });
        }
    }

}
