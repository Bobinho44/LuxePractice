package fr.bobinho.luxepractice.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setspawn")
public class SetSpawnCommand extends BaseCommand {

    /**
     * Command setspawn
     *
     * @param sender the sender
     */
    @Default
    @Syntax("/setspawn")
    @CommandPermission("luxepractice.setspawn")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Sets the spawn
            PracticeSettings.getConfiguration().set("spawn", PracticeLocationUtil.getAsString(player.getLocation()));
            PracticeSettings.save();

            //Sends the message
            player.sendMessage(ChatColor.GREEN + "You have defined the new spawn.");
        }
    }

}
