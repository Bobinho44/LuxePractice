package fr.bobinho.luxepractice.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.location.LocationUtil;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
public class SpawnCommand extends BaseCommand {

    @Default
    @CommandPermission("luxepractice.spawn")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Location spawn = LocationUtil.getAsLocation(PracticeSettings.getConfiguration().getString("spawn"));
            player.teleport(spawn);
        }
    }

}