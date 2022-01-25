package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.atlanmod.commons.Guards;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DuelMatch extends PracticeMatch {

    private final PracticePlayer player1;
    private final PracticePlayer player2;
    private PracticePlayer winner;

    public DuelMatch(@NotNull PracticeArena arena, @Nonnull PracticePlayer player1, @Nonnull PracticePlayer player2) {
        super(arena);

        Guards.checkNotNull(player1, "player1 is null");
        Guards.checkNotNull(player2, "player2 is null");

        this.player1 = player1;
        this.player2 = player2;
    }

    private PracticePlayer getPlayer1() {
        return player1;
    }

    private PracticePlayer getPlayer2() {
        return player2;
    }

    @Nullable
    private PracticePlayer getWinner() {
        return winner;
    }

    @Nullable
    private PracticePlayer getLooser() {
        return getWinner().equals(getPlayer1()) ? getPlayer2() : getPlayer1();
    }

    public void setWinner(PracticePlayer winner) {
        this.winner = winner;
    }

    @Override
    public BaseComponent[] getStartMessageForFighters(@Nonnull PracticePlayer receiver) {
        Guards.checkNotNull(receiver, "receiver is null");

        return new ComponentBuilder("Duel request accepted vs ").color(ChatColor.GOLD).bold(true)
                .append((getPlayer1().equals(receiver) ? getPlayer2() : getPlayer1()).getName()).color(ChatColor.YELLOW).bold(false)
                .append("\nThe match will automatically end in ").color(ChatColor.GOLD)
                .append("30 ").color(ChatColor.YELLOW)
                .append("minutes").color(ChatColor.GOLD)
                .append("\n\nGood luck!").color(ChatColor.GREEN).create();
    }

    @Override
    public BaseComponent[] getEndMessageForFighters(@Nonnull PracticePlayer receiver) {
        Guards.checkNotNull(receiver, "receiver is null");

        return new ComponentBuilder("Winner: ").color(ChatColor.GOLD).bold(true)
                .append(getWinner().getName()).color(ChatColor.GREEN).bold(false)
                .append("\nInventories (click to view): ").color(ChatColor.GOLD)
                .append(getWinner().getClickableInventoryAccessAsString()).color(ChatColor.GREEN)
                .append(", ").color(ChatColor.GRAY)
                .append(getLooser().getClickableInventoryAccessAsString()).color(ChatColor.RED)
                .append("\nMatch Duration: ").color(ChatColor.GOLD)
                .append(DurationFormatUtils.formatDurationHMS(getDuration().elapsed().toMillis())).color(ChatColor.YELLOW).create();
    }

    @Override
    public BaseComponent[] getEndMessageForEveryone(@Nonnull PracticePlayer receiver) {
        Guards.checkNotNull(receiver, "receiver is null");

        return new ComponentBuilder("[Match] ").color(ChatColor.GOLD)
                .append(getLooser().getName()).color(ChatColor.RED)
                .append(" was defeated by ").color(ChatColor.AQUA)
                .append(getWinner().getName()).color(ChatColor.GREEN).create();
    }

    @Override
    public List<PracticePlayer> getALlMembers() {
        return List.of(getPlayer1(), getPlayer2());
    }

}
