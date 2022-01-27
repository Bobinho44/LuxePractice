package fr.bobinho.luxepractice.utils.arena.match;

import fr.bobinho.luxepractice.utils.arena.PracticeArena;
import fr.bobinho.luxepractice.utils.format.PracticeDurationFormat;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnonymousMatch extends PracticeMatch {

    private final PracticePlayer player1;
    private final PracticePlayer player2;
    private final PracticeKit kit;
    private PracticePlayer winner;

    public AnonymousMatch(@NotNull PracticeArena arena, @Nonnull PracticePlayer player1, @Nonnull PracticePlayer player2, @Nonnull PracticeKit kit) {
        super(arena);

        Guards.checkNotNull(player1, "player1 is null");
        Guards.checkNotNull(player2, "player2 is null");
        Guards.checkNotNull(kit, "kit is null");

        this.player1 = player1;
        this.player2 = player2;
        this.kit = kit;
    }

    private PracticeKit getKit() {
        return kit;
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
    public BaseComponent[] getStartMessage(@Nonnull PracticePlayer receiver) {
        Guards.checkNotNull(receiver, "receiver is null");

        return new ComponentBuilder("Anonymous match found vs ").color(ChatColor.GOLD).bold(true)
                .append((getPlayer1().equals(receiver) ? getPlayer2() : getPlayer1()).getName()).color(ChatColor.YELLOW).bold(false)
                .append("\nThe match will automatically end in ").color(ChatColor.GOLD)
                .append("30 ").color(ChatColor.YELLOW)
                .append("minutes").color(ChatColor.GOLD)
                .append("\n\nGood luck!").color(ChatColor.GREEN).create();
    }

    @Override
    public BaseComponent[] getEndMessage() {
        Guards.checkNotNull(getLooser(), "looser is null");
        Guards.checkNotNull(getWinner(), "winner is null");

        return new ComponentBuilder("Winner: ").color(ChatColor.GOLD).bold(true)
                .append(getWinner().getName()).color(ChatColor.GREEN).bold(false)
                .append("\nInventories (click to view): ").color(ChatColor.GOLD)
                .append(getWinner().getClickableInventoryAccessAsString()).color(ChatColor.GREEN)
                .append(", ").color(ChatColor.GRAY)
                .append(getLooser().getClickableInventoryAccessAsString()).color(ChatColor.RED)
                .append("\nMatch Duration: ").color(ChatColor.GOLD)
                .append(PracticeDurationFormat.getAsMinuteSecondFormat(getDuration().elapsed().toSeconds())).color(ChatColor.YELLOW).create();
    }

    @Override
    public BaseComponent[] getBroadcastMessage() {
        Guards.checkNotNull(getLooser(), "looser is null");
        Guards.checkNotNull(getWinner(), "winner is null");

        return new ComponentBuilder("[Match] ").color(ChatColor.GOLD)
                .append(getLooser().getName()).color(ChatColor.RED)
                .append(" was defeated by ").color(ChatColor.AQUA)
                .append(getWinner().getName()).color(ChatColor.GREEN).create();
    }

    @Override
    public List<PracticePlayer> getALlMembers() {
        return Stream.concat(List.of(getPlayer1(), getPlayer2()).stream(), getSpectators().stream()).collect(Collectors.toList());
    }

    @Override
    public boolean isFinished() {
        return getDeathPracticePlayers().size() > 0;
    }

    @Override
    public void start() {
        super.start();
        for (PracticePlayer practicePlayer : getALlMembers()) {
            practicePlayer.teleportAroundLocation(getArena().getSpawn());
            practicePlayer.removeAllPotionEffects();
            PracticeKitManager.givePracticeKit(practicePlayer, getKit());
            practicePlayer.getSpigotPlayer().get().sendMessage(getStartMessage(practicePlayer));
        }
    }

    @Override
    public void end() {
        setWinner(getDeathPracticePlayers().equals(getPlayer1()) ? getPlayer2() : getPlayer1());
        for (PracticePlayer practicePlayer : getALlMembers()) {
            if (isDeadFighter(practicePlayer)) {
                PracticeMatchManager.addOldFighterAsSpectator(practicePlayer);
            }
            practicePlayer.removeAllPotionEffects();
            practicePlayer.getSpigotPlayer().get().sendMessage(getEndMessage());
        }

        for (Player player : Bukkit.getOnlinePlayers().stream().filter(player -> getALlMembers().contains(PracticePlayerManager.getPracticePlayer(player.getUniqueId()))).collect(Collectors.toList())) {
            player.sendMessage(getBroadcastMessage());
        }
    }

}
