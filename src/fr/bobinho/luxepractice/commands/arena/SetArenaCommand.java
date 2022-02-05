package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setarena")
public class SetArenaCommand extends BaseCommand {

    /**
     * Command setarena
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/setarena <name>")
    @CommandPermission("luxepractice.setarena")
    public void onSetArenaCommand(CommandSender commandSender, @Single String arenaName, @Single String loc1, @Single String loc2) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Checks if the arena name is already used
                if (PracticeArenaManager.isItPracticeArena(arenaName)) {
                    practiceSender.sendMessage(ChatColor.RED + "There is already an arena named " + arenaName + "!");
                    return;
                }

                //Gets spawns
                Location spawn1 = PracticeLocationUtil.getAsLocation(loc1 + ":0:0");
                Location spawn2 = PracticeLocationUtil.getAsLocation(loc2 + ":0:0");

                //Creates the arena
                PracticeArenaManager.createPracticeArena(spawn1, spawn2, arenaName);

                //Sends the message
                practiceSender.sendMessage(ChatColor.GREEN + "You have set the " + arenaName + " arena.");
            });
        }
    }

}