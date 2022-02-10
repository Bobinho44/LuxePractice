package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeam;
import fr.bobinho.luxepractice.utils.arena.team.PracticeTeamManager;
import fr.bobinho.luxepractice.utils.format.PracticeDurationFormat;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TeamDuel extends PracticeMatch {

    /**
     * Fields
     */
    private final PracticeTeam blue;
    private final PracticeTeam red;
    private PracticeTeam winner;

    /**
     * Creates a new team duel practice match
     *
     * @param arena the practice arena
     * @param blue  the blue practice team
     * @param red   the red practice team
     */
    public TeamDuel(@Nonnull PracticeArena arena, @Nonnull PracticeTeam blue, @Nonnull PracticeTeam red) {
        super(arena);

        Validate.notNull(blue, "blue is null");
        Validate.notNull(red, "red is null");

        this.blue = blue;
        this.blue.setName("Blue Team");
        this.blue.setColor(ChatColor.BLUE);
        this.red = red;
        this.red.setName("Red Team");
        this.red.setColor(ChatColor.RED);

        Stream.concat(blue.getMembers().stream(), red.getMembers().stream()).collect(Collectors.toSet()).forEach(this::addFighter);

        //Starts the practice match
        start();
    }

    /**
     * Gets the practice match blue practice team
     *
     * @return the practice match blue practice team
     */
    @Nonnull
    public PracticeTeam getBlueTeam() {
        return blue;
    }

    /**
     * Gets the practice match red practice team
     *
     * @return the practice match red practice team
     */
    @Nonnull
    public PracticeTeam getRedTeam() {
        return red;
    }

    private int getLivingPracticePlayerNumber(@Nonnull PracticeTeam practiceTeam) {
        Validate.notNull(practiceTeam, "practiceTeam is null");

        return (int) practiceTeam.getMembers().stream().filter(member -> !getDeadFighters().contains(member)).count();
    }

    /**
     * Gets the practice match winner
     *
     * @return the practice match winner
     */
    @Nullable
    public PracticeTeam getWinner() {
        return winner;
    }

    /**
     * Gets the practice match looser
     *
     * @return the practice match looser
     */
    @Nullable
    private PracticeTeam getLooser() {
        return getWinner() == null ? null : getWinner().equals(getBlueTeam()) ? getRedTeam() : getBlueTeam();
    }

    /**
     * Sets the practice match winner
     *
     * @param winner the practice match winner
     */
    public void setWinner(PracticeTeam winner) {
        this.winner = winner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        super.start();
        for (PracticePlayer practicePlayer : getALlMembers()) {
            practicePlayer.saveOldInventory();
            if (getBlueTeam().getMembers().contains(practicePlayer)) {
                practicePlayer.teleport(getArena().getSpawn1());
            }
            else {
                practicePlayer.teleport(getArena().getSpawn2());
            }
            practicePlayer.removeAllPotionEffects();
            practicePlayer.changeName(PracticeTeamManager.getPracticeTeam(practicePlayer).get().getColor());
            practicePlayer.sendMessage(getStartMessage(practicePlayer));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finish() {
        setWinner(getLivingPracticePlayerNumber(getBlueTeam()) == 0 ? getRedTeam() : getBlueTeam());
        super.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mustFinish() {
        return !isFinished() && (getLivingPracticePlayerNumber(getRedTeam()) == 0 || getLivingPracticePlayerNumber(getBlueTeam()) == 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public BaseComponent @NotNull [] getStartMessage(@Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceReceiver, "receiver is null");

        //Creates the start message
        return new ComponentBuilder("Teamfight starting vs ").color(ChatColor.GOLD).bold(true)
                .append(PracticeTeamManager.getPracticeTeam(practiceReceiver).get().getLeader().getName()).color(ChatColor.YELLOW).bold(false)
                .append("'s team").color(ChatColor.GOLD).bold(true)
                .append("\nBlue Team: ").color(ChatColor.BLUE).bold(true)
                .append(getBlueTeam().getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", "))).bold(false)
                .append("\nRed Team: ").color(ChatColor.RED).bold(true)
                .append(getRedTeam().getMembers().stream().map(PracticePlayer::getName).collect(Collectors.joining(", "))).bold(false)
                .append("\nThe match will automatically end in ").color(ChatColor.GOLD)
                .append("30 ").color(ChatColor.YELLOW)
                .append("minutes").color(ChatColor.GOLD).create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public BaseComponent[] getFinishMessage() {
        Validate.notNull(getWinner(), "winner is null");

        //Creates the finish message
        return new ComponentBuilder("Winner: ").color(ChatColor.GOLD).bold(true)
                .append(getWinner().getName()).color(getWinner().getColor()).bold(false)
                .append("\nInventories (click to view): ").color(ChatColor.GOLD)
                .append(getWinner().getMembersClickableInventoryAccessAsString())
                .append(" / ").color(ChatColor.GRAY)
                .append(Objects.requireNonNull(getLooser()).getMembersClickableInventoryAccessAsString())
                .append("\nMatch Duration: ").color(ChatColor.GOLD)
                .append(PracticeDurationFormat.getAsMinuteSecondFormat(getDuration().elapsed(TimeUnit.SECONDS))).color(ChatColor.YELLOW).create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public BaseComponent[] getBroadcastFinishMessage() {
        Validate.notNull(getWinner(), "winner is null");

        //Gets the broadcast finish message
        return new ComponentBuilder("[Teamfight] ").color(ChatColor.GOLD)
                .append(Objects.requireNonNull(getLooser()).getName()).color(ChatColor.RED)
                .append(" was defeated by ").color(ChatColor.AQUA)
                .append(getWinner().getName()).color(ChatColor.GREEN).create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public String getMatchInfoAsString() {
        return ChatColor.GOLD + "Teamfight - " + (getWinner() != null ? getWinner().getColor() + "Winner : " + getWinner().getName() :
                "Remaining player: " + ChatColor.BLUE + getLivingPracticePlayerNumber(getBlueTeam()) + ChatColor.GOLD + " - " +
                        ChatColor.RED + getLivingPracticePlayerNumber(getRedTeam()));
    }

}