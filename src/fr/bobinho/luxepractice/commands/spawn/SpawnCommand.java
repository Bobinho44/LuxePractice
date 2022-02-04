package fr.bobinho.luxepractice.commands.spawn;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandAlias("spawn")
public class SpawnCommand extends BaseCommand {

    /**
     * Command spawn
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/spawn")
    @CommandPermission("luxepractice.spawn")
    public void onSpawnCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {

            //Teleports the practice player to the spawn
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(PracticePlayer::teleportToTheSpawn);
        }
    }

}