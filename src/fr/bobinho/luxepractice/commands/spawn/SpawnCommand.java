package fr.bobinho.luxepractice.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
public class SpawnCommand extends BaseCommand {

    /**
     * Command spawn
     *
     * @param sender the sender
     */
    @Default
    @CommandPermission("luxepractice.spawn")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Gets and teleports the player to the spawn
            Location spawn = PracticeLocationUtil.getAsLocation(PracticeSettings.getConfiguration().getString("spawn"));
            player.teleport(spawn);
        }
    }

}