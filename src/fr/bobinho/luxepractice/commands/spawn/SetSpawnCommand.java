package fr.bobinho.luxepractice.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
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
    @CommandPermission("luxepractice.setspawn")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Sets the spawn
            PracticeSettings.getConfiguration().set("spawn", player.getLocation());
        }
    }

}
