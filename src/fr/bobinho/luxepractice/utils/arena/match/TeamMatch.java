package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.atlanmod.commons.Guards;
import org.atlanmod.commons.time.Stopwatch;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeamMatch implements PracticeMatch {

    private enum TeamMatchResult {
        BLUE("Blue Team", ChatColor.BLUE),
        RED("Red Team", ChatColor.RED);

        private String name;
        private ChatColor color;

        TeamMatchResult(String name, ChatColor color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public ChatColor getColor() {
            return color;
        }

    }

    private final PracticeTeam blue;
    private final PracticeTeam red;
    private final PracticeArena arena;
    private TeamMatchResult winner;
    private Stopwatch duration;

    public TeamMatch(@Nonnull PracticeTeam blue, @Nonnull PracticeTeam red, @Nonnull PracticeArena arena) {
        Guards.checkNotNull(blue, "blue is null");
        Guards.checkNotNull(red, "red is null");
        Guards.checkNotNull(arena, "arena is null");

        this.blue = blue;
        this.red = red;
        this.arena = arena;
        this.duration = Stopwatch.createStarted();
    }

    public PracticeTeam getBlueTeam() {
        return blue;
    }

    public PracticeTeam getRedTeam() {
        return red;
    }

    public PracticeArena getArena() {
        return arena;
    }

    @Nullable
    public TeamMatchResult getWinner() {
        return winner;
    }

    public void setWinner(TeamMatchResult winner) {
        this.winner = winner;
    }

    public Stopwatch getDuration() {
        return duration;
    }

    @Override
    public BaseComponent[] getStartMessage(@Nonnull PracticePlayer receiver) {
        Guards.checkNotNull(receiver, "receiver is null");
        Guards.checkArgument(PracticeTeamManager.hasPracticeTeam(receiver), "receiver doesn't have team");

        return new ComponentBuilder("Teamfight starting vs ").color(ChatColor.GOLD).bold(true)
                .insertion(PracticeTeamManager.getPracticePlayerTeam(receiver).get().getLeader().getName()).color(ChatColor.YELLOW)
                .insertion("'s team").color(ChatColor.GOLD).bold(true)
                .append("Blue Team: ").color(ChatColor.BLUE).bold(true)
                .insertion(getBlueTeam().getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", ")))
                .append("Red Team: ").color(ChatColor.RED).bold(true)
                .insertion(getRedTeam().getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", ")))
                .append("The match will automatically end in ").color(ChatColor.GOLD)
                .insertion("30").color(ChatColor.YELLOW)
                .insertion("minutes").color(ChatColor.GOLD).create();
    }

    @Override
    public BaseComponent[] getEndMessage(@Nonnull PracticePlayer receiver) {
        Guards.checkNotNull(receiver, "receiver is null");
        Guards.checkArgument(PracticeTeamManager.hasPracticeTeam(receiver), "receiver doesn't have team");

        return new ComponentBuilder("Winner: ").color(ChatColor.GOLD).bold(true)
                .insertion(getWinner().getName()).color(getWinner().getColor())
                .append("Inventories (click to view): ").color(ChatColor.GOLD)
                .append(getBlueTeam().getMembersWithInventory())
                .append(getRedTeam().getMembersWithInventory())
                .append("Match Duration: ").color(ChatColor.GOLD)
                .insertion(DurationFormatUtils.formatDurationHMS(getDuration().elapsed().toMillis())).color(ChatColor.YELLOW).create();
    }

    @Override
    public List<PracticePlayer> getALlMembers() {
        return Stream.concat(getBlueTeam().getMembers().stream(), getRedTeam().getMembers().stream()).collect(Collectors.toList());
    }

}
