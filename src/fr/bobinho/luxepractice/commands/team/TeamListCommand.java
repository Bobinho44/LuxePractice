package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@CommandAlias("teamlist")
public class TeamListCommand extends BaseCommand {

    /**
     * Command teamlist
     *
     * @param commandSender the sender
     */
    @Default
    @Syntax("/teamlist")
    @CommandPermission("luxepractice.teamlist")
    public void onTeamListCommand(CommandSender commandSender) {
        if (commandSender instanceof Player) {
            PracticePlayerManager.getPracticePlayer(((Player) commandSender).getUniqueId()).ifPresent(practiceSender -> {

                //Creates practice team info list
                ComponentBuilder practiceTeamList = new ComponentBuilder(ChatColor.GOLD + "Team list: ");

                //Lists all practice team
                for (PracticeTeam practiceTeam : PracticeTeamManager.getPracticeTeams()) {

                    //Adds hover with practice team members
                    TextComponent practiceTeamInfo = new TextComponent(ChatColor.GREEN + " - " + practiceTeam.getLeader().getName() + "'s team");
                    practiceTeamInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, List.of(new Text(practiceTeam.getMembersAsString()))));
                    practiceTeamList.append("\n").append(practiceTeamInfo);
                }

                //Sends message
                practiceSender.sendMessage(practiceTeamList.create());
            });
        }
    }

}