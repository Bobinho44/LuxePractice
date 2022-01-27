package fr.bobinho.luxepractice.utils.arena.team;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.atlanmod.commons.Guards;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class PracticeTeam {

    private final PracticePlayer leader;
    private List<PracticePlayer> members;
    private String name;
    private ChatColor color;

    public PracticeTeam(@Nonnull PracticePlayer leader) {
        Guards.checkNotNull(leader, "leader is null");

        this.leader = leader;
        this.members = List.of(leader);
    }

    public void setName(@Nonnull String name) {
        Guards.checkNotNull(name, "name is null");

        this.name = name;
    }

    public void setColor(@Nonnull ChatColor color) {
        Guards.checkNotNull(color, "color is null");

        this.color = color;
    }

    @Nullable
    public ChatColor getColor() {
        return color;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public PracticePlayer getLeader() {
        return leader;
    }

    public List<PracticePlayer> getMembers() {
        return members;
    }

    public void addMember(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        getMembers().add(practicePlayer);
    }

    public void removeMember(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        getMembers().remove(practicePlayer);
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
