package fr.bobinho.luxepractice.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setspawn")
public class SetSpawnCommand extends BaseCommand {

    /**
     * Command setspawn
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/setspawn <world>")
    @CommandCompletion("world|world_nether|world_the_end")
    @CommandPermission("luxepractice.setspawn")
    public void onSetSpawnCommand(CommandSender commandSender, @Single String worldType) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (!(worldType.equals("world") || worldType.equals("world_nether") || worldType.equals("world_the_end"))) {
                player.sendMessage(ChatColor.RED + "The world " + worldType + " doesn't exist!");
                return;
            }

            //Sets the spawn
            LuxePracticeCore.getMainSettings().getConfiguration().set("spawn." + worldType, PracticeLocationUtil.getAsString(player.getLocation()));
            LuxePracticeCore.getMainSettings().save();

            //Sends the message
            player.sendMessage(ChatColor.GREEN + "You have defined the new spawn.");
        }
    }

}