package fr.bobinho.luxepractice.commands.arena;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Single;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("setarena")
public class SetArenaCommand extends BaseCommand {

    /**
     * Command setarena
     *
     * @param sender the sender
     */
    @Default
    @CommandPermission("luxepractice.setarena")
    public void onDefault(CommandSender sender, @Single String arenaName) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            //Checks if the arena name is already used
            if (PracticeArenaManager.isItPracticeArena(arenaName)) {
                player.sendMessage(ChatColor.RED + "There is already an arena named " + arenaName + "!");
                return;
            }

            //Creates the arena
            PracticeArenaManager.createPracticeArena(player.getLocation(), arenaName);
        }
    }

}