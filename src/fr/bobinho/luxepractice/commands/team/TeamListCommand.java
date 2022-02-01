package fr.bobinho.luxepractice.commands.team;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
            Player sender = (Player) commandSender;

            //Creates practice team info list
            StringBuilder practiceTeamList = new StringBuilder(ChatColor.GOLD + "Team list: ");
            for (PracticeTeam practiceTeam : PracticeTeamManager.getPracticeTeams()) {

                //Adds hover with practice team members
                TextComponent practiceTeamInfo = new TextComponent(ChatColor.GREEN + " - " + practiceTeam.getName());
                practiceTeamInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(practiceTeam.getMembersAsString()).create()));
                practiceTeamList.append("\n").append(practiceTeamInfo);
            }

            //Sends message
            sender.sendMessage(practiceTeamList.toString());
        }
    }

}