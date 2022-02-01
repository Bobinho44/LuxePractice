package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.format.PracticeDurationFormat;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeamDuel extends PracticeMatch {

    private final PracticeTeam blue;
    private final PracticeTeam red;
    private PracticeTeam winner;

    public TeamDuel(@Nonnull PracticeArena arena, @Nonnull PracticeTeam blue, @Nonnull PracticeTeam red) {
        super(arena);

        Guards.checkNotNull(blue, "blue is null");
        Guards.checkNotNull(red, "red is null");

        this.blue = blue;
        this.blue.setName("Blue Team");
        this.blue.setColor(ChatColor.BLUE);
        this.red = red;
        this.red.setName("Red Team");
        this.red.setColor(ChatColor.RED);
    }

    private PracticeTeam getBlueTeam() {
        return blue;
    }

    private PracticeTeam getRedTeam() {
        return red;
    }

    @Nullable
    private PracticeTeam getWinner() {
        return winner;
    }

    @Nullable
    private PracticeTeam getLooser() {
        return winner.equals(getBlueTeam()) ? getRedTeam() : getBlueTeam();
    }

    public void setWinner(PracticeTeam winner) {
        this.winner = winner;
    }


    @Override
    public BaseComponent[] getStartMessage(@Nonnull PracticePlayer receiver) {
        Guards.checkNotNull(receiver, "receiver is null");
        Guards.checkArgument(PracticeTeamManager.hasPracticeTeam(receiver), "receiver doesn't have team");

        return new ComponentBuilder("Teamfight starting vs ").color(ChatColor.GOLD).bold(true)
                .append(PracticeTeamManager.getPracticePlayerTeam(receiver).get().getLeader().getName()).color(ChatColor.YELLOW).bold(false)
                .append("'s team").color(ChatColor.GOLD).bold(true)
                .append("\nBlue Team: ").color(ChatColor.BLUE).bold(true)
                .append(getBlueTeam().getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", "))).bold(false)
                .append("\nRed Team: ").color(ChatColor.RED).bold(true)
                .append(getRedTeam().getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", "))).bold(false)
                .append("\nThe match will automatically end in ").color(ChatColor.GOLD)
                .append("30 ").color(ChatColor.YELLOW)
                .append("minutes").color(ChatColor.GOLD).create();
    }

    @Override
    public BaseComponent[] getEndMessage() {
        Guards.checkNotNull(getLooser(), "looser is null");
        Guards.checkNotNull(getWinner(), "winner is null");

        return new ComponentBuilder("Winner: ").color(ChatColor.GOLD).bold(true)
                .append(getWinner().getName()).color(getWinner().getColor()).bold(false)
                .append("\nInventories (click to view): ").color(ChatColor.GOLD)
                .append(getPracticeTeamMembersClickableInventoryAccessAsString(getWinner()))
                .append(getPracticeTeamMembersClickableInventoryAccessAsString(getLooser()))
                .append("\nMatch Duration: ").color(ChatColor.GOLD)
                .append(PracticeDurationFormat.getAsMinuteSecondFormat(getDuration().elapsed().toSeconds())).color(ChatColor.YELLOW).create();
    }

    @Override
    public BaseComponent[] getBroadcastMessage() {
        Guards.checkNotNull(getLooser(), "looser is null");
        Guards.checkNotNull(getWinner(), "winner is null");

        return new ComponentBuilder("[Teamfight] ").color(ChatColor.GOLD)
                .append(getLooser().getName()).color(ChatColor.RED)
                .append(" was defeated by ").color(ChatColor.AQUA)
                .append(getWinner().getName()).color(ChatColor.GREEN).create();
    }

    private BaseComponent[] getPracticeTeamMembersClickableInventoryAccessAsString(PracticeTeam practiceTeam) {
        ComponentBuilder builder = new ComponentBuilder();
        for (PracticePlayer practicePlayer : practiceTeam.getMembers()) {
            builder.append(practicePlayer.getClickableInventoryAccessAsString()).color(practiceTeam.getColor())
                    .append(", ").color(ChatColor.GRAY);
        }
        builder.removeComponent(builder.getCursor());
        return builder.create();
    }

    public boolean isBlueTeamMember(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(PracticeTeamManager.hasPracticeTeam(practicePlayer), "practicePlayer doesn't have team");

        return getBlueTeam().getMembers().contains(practicePlayer);
    }

    @Override
    public List<PracticePlayer> getALlMembers() {
        if (isEnded()) return getSpectators();
        return Stream.of(getBlueTeam().getMembers().stream(), getRedTeam().getMembers().stream(), getSpectators().stream()).flatMap(i -> i).collect(Collectors.toList());
    }

    private int getLivingPracticePlayerNumber(@Nonnull PracticeTeam practiceTeam) {
        Guards.checkNotNull(practiceTeam, "practiceTeam is null");

        return practiceTeam.getMembers().stream().filter(member -> !getDeathPracticePlayers().contains(member)).collect(Collectors.toList()).size();
    }

    @Override
    public boolean isFinished() {
        return getLivingPracticePlayerNumber(getRedTeam()) == 0 || getLivingPracticePlayerNumber(getBlueTeam()) == 0;
    }

    @Override
    public void start() {
        for (PracticePlayer practicePlayer : getALlMembers()) {
            practicePlayer.saveOldInventory();

            practicePlayer.teleportAroundLocation(getArena().getSpawn());
            practicePlayer.removeAllPotionEffects();
            practicePlayer.sendMessage(getStartMessage(practicePlayer));
            practicePlayer.changeName((isBlueTeamMember(practicePlayer) ? ChatColor.BLUE : ChatColor.RED) + practicePlayer.getName());
        }
    }

    @Override
    public void end() {
        setWinner(getLivingPracticePlayerNumber(getBlueTeam()) == 0 ? getRedTeam() : getBlueTeam());
        for (PracticePlayer practicePlayer : getALlMembers()) {
            if (!isDeadFighter(practicePlayer)) {
                PracticeMatchManager.addOldFighterAsSpectator(practicePlayer);
            }
            practicePlayer.removeAllPotionEffects();
            practicePlayer.sendMessage(getEndMessage());
        }

        for (Player player : Bukkit.getOnlinePlayers().stream().filter(player -> getALlMembers().contains(PracticePlayerManager.getPracticePlayer(player.getUniqueId()))).collect(Collectors.toList())) {
            player.sendMessage(getBroadcastMessage());
        }
        setEnd();
    }

    @Override
    public String getMatchInfo() {
        return ChatColor.GOLD + "Teamfight - " + (getWinner() != null ? getWinner().getColor() + "Winner : " + getWinner().getName() :
                "Remaining player: " + ChatColor.BLUE + getLivingPracticePlayerNumber(getBlueTeam()) + ChatColor.GOLD + " - " +
                        ChatColor.RED + getLivingPracticePlayerNumber(getRedTeam()));
    }

    @Override
    public List<PracticePlayer> getFighters() {
        return Stream.concat(getBlueTeam().getMembers().stream(), getRedTeam().getMembers().stream()).collect(Collectors.toList());
    }
}
