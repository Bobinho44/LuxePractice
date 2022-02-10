package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.format.PracticeDurationFormat;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DuelMatch extends PracticeMatch {

    /**
     * Fields
     */
    private final PracticePlayer fighter1;
    private final PracticePlayer fighter2;
    private final PracticeKit kit;
    private PracticePlayer winner;

    /**
     * Creates a new duel practice match
     *
     * @param arena    the practice arena
     * @param fighter1 the practice fighter 1
     * @param fighter2 the practice fighter 2
     * @param kit      the practice kit
     */
    public DuelMatch(@NotNull PracticeArena arena, @Nonnull PracticePlayer fighter1, @Nonnull PracticePlayer fighter2, @Nonnull PracticeKit kit) {
        super(arena);

        Validate.notNull(fighter1, "fighter1 is null");
        Validate.notNull(fighter2, "fighter2 is null");
        Validate.notNull(kit, "kit is null");

        this.fighter1 = fighter1;
        this.fighter2 = fighter2;
        this.kit = kit;

        List.of(fighter1, fighter2).forEach(this::addFighter);

        //Starts the practice match
        start();
    }

    /**
     * Gets the practice match fighter 1
     *
     * @return the practice match fighter 1
     */
    @Nonnull
    public PracticePlayer getFighter1() {
        return fighter1;
    }

    /**
     * Gets the practice match fighter 2
     *
     * @return the practice match fighter 2
     */
    @Nonnull
    public PracticePlayer getFighter2() {
        return fighter2;
    }

    /**
     * Gets the practice match kit
     *
     * @return the practice match kit
     */
    @Nonnull
    public PracticeKit getKit() {
        return kit;
    }

    /**
     * Gets the practice match winner
     *
     * @return the practice match winner
     */
    @Nullable
    public PracticePlayer getWinner() {
        return winner;
    }

    /**
     * Gets the practice match looser
     *
     * @return the practice match looser
     */
    @Nullable
    private PracticePlayer getLooser() {
        return getWinner() == null ? null : getWinner().equals(getFighter1()) ? getFighter2() : getFighter1();
    }

    /**
     * Sets the practice match winner
     *
     * @param winner the practice match winner
     */
    public void setWinner(PracticePlayer winner) {
        this.winner = winner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() {
        super.start();
        for (PracticePlayer practicePlayer : getFighters()) {
            practicePlayer.saveOldInventory();
            if (getFighter1().equals(practicePlayer)) {
                practicePlayer.teleport(getArena().getSpawn1());
            }
            else {
                practicePlayer.teleport(getArena().getSpawn2());
            }
            practicePlayer.removeAllPotionEffects();
            PracticeKitManager.givePracticeKit(practicePlayer, getKit());
            practicePlayer.sendMessage(getStartMessage(practicePlayer));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finish() {
        setWinner(getFighters().stream().filter(practiceFighter -> !getDeadFighters().contains(practiceFighter)).findFirst().get());
        super.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean mustFinish() {
        return !isFinished() && getDeadFighters().size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public BaseComponent @NotNull [] getStartMessage(@Nonnull PracticePlayer practiceReceiver) {
        Validate.notNull(practiceReceiver, "receiver is null");

        //Creates the start message
        return new ComponentBuilder("Duel request accepted vs ").color(ChatColor.GOLD).bold(true)
                .append((getFighter1().equals(practiceReceiver) ? getFighter2() : getFighter1()).getName()).color(ChatColor.YELLOW).bold(false)
                .append("\nThe match will automatically end in ").color(ChatColor.GOLD)
                .append("30 ").color(ChatColor.YELLOW)
                .append("minutes").color(ChatColor.GOLD)
                .append("\n\nGood luck!").color(ChatColor.GREEN).create();
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
                .append(getWinner().getName()).color(ChatColor.GREEN).bold(false)
                .append("\nInventories (click to view): ").color(ChatColor.GOLD)
                .append(getWinner().getClickableInventoryAccessAsString()).color(ChatColor.GREEN)
                .append(", ").color(ChatColor.GRAY)
                .append(Objects.requireNonNull(getLooser()).getClickableInventoryAccessAsString()).color(ChatColor.RED)
                .append("\nMatch Duration: ").color(ChatColor.GOLD)
                .append(PracticeDurationFormat.getAsMinuteSecondFormat(getDuration().elapsed(TimeUnit.SECONDS))).color(ChatColor.YELLOW).create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public BaseComponent[] getBroadcastFinishMessage() {
        Validate.notNull(getLooser(), "looser is null");
        Validate.notNull(getWinner(), "winner is null");

        //Gets the broadcast finish message
        return new ComponentBuilder("[Duel] ").color(ChatColor.GOLD)
                .append(getLooser().getName()).color(ChatColor.RED)
                .append(" was defeated by ").color(ChatColor.AQUA)
                .append(getWinner().getName()).color(ChatColor.GREEN).create();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Nonnull
    public String getMatchInfoAsString() {
        return ChatColor.GOLD + "Duel: " + ChatColor.GREEN + (getWinner() != null ? "Winner : " + getWinner().getName() : "Not finished");
    }

}