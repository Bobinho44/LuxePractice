package fr.bobinho.luxepractice.commands.kit;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setkit")
public class SetKitCommand extends BaseCommand {

    /**
     * Command setkit
     *
     * @param commandSender    the sender
     * @param defaultKitName   the kit name
     * @param isMainDefaultKit the kit main statue
     */
    @Default
    @Syntax("/setkit <name> <isMainDefault = true|false>")
    @CommandPermission("luxepractice.setkit")
    public void onSetKitCommand(CommandSender commandSender, @Single String defaultKitName, @Single String isMainDefaultKit) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if the default practice kit name is already use
                if (PracticeKitManager.isItBasicPracticeKit(defaultKitName)) {
                    practiceSender.sendMessage(ChatColor.RED + "There is already a default kit named " + defaultKitName + "!");
                    return;
                }

                //Creates the default practice kit
                PracticeKitManager.createBasicPracticeKit(defaultKitName, PracticeKitManager.getPracticePlayerInventoryAsKit(practiceSender), Boolean.parseBoolean(isMainDefaultKit));
                practiceSender.sendMessage(ChatColor.GREEN + "You have created the default kit " + defaultKitName + ".");
            });
        }
    }

}