package fr.bobinho.luxepractice.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setspawn")
public class SetSpawnCommand extends BaseCommand {

    @Default
    @CommandPermission("luxepractice.setspawn")
    public void onDefault(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            PracticeSettings.getConfiguration().set("spawn", player.getLocation());
        }
    }

}
