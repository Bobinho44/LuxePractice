package fr.bobinho.luxepractice.utils.arena.team;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.atlanmod.commons.Guards;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class PracticeTeam {

    private PracticePlayer leader;
    private List<PracticePlayer> members;

    public PracticeTeam(@Nonnull PracticePlayer leader) {
        Guards.checkNotNull(leader, "leader is null");

        this.leader = leader;
        this.members = List.of(leader);
    }

    public PracticePlayer getLeader() {
        return leader;
    }

    public List<PracticePlayer> getMembers() {
        return members;
    }

    public String getMembersAsString() {
        return getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", "));
    }

    public TextComponent getMembersWithInventory() {
        TextComponent base = new TextComponent();
        for (PracticePlayer player : getMembers()) {
            TextComponent playerInfo = new TextComponent(player.getName());
            playerInfo.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/practiceinventory " + player.getUuid()));
            base.addExtra(playerInfo);
            TextComponent separator = new TextComponent(", ");
            base.addExtra(separator);
        }
        return base;
    }

}
