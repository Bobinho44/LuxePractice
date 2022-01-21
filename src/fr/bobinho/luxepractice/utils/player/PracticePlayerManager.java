package fr.bobinho.luxepractice.utils.player;

import org.atlanmod.commons.Guards;
import org.atlanmod.commons.Preconditions;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class PracticePlayerManager {

    private static final List<PracticePlayer> practicePlayers = new ArrayList<PracticePlayer>();

    private static List<PracticePlayer> getPracticePlayers() {
        return practicePlayers;
    }

    private static boolean isAlreadyRegistered(@Nonnull PracticePlayer practicePlayer) {
        Preconditions.requireThat(practicePlayer).isNotNull();

        return getPracticePlayers().contains(practicePlayer);
    }

    private static boolean isAlreadyRegistered(Player player) {
        return getPracticePlayers().stream().anyMatch(practicePlayer -> practicePlayer.getUuid().equals(player.getUniqueId()));
    }

    public static PracticePlayer getPlayer(Player player) {
        Guards.checkArgument(isAlreadyRegistered(player), "player is not registered");

        return getPracticePlayers().stream().filter(practicePlayer -> practicePlayer.getUuid().equals(player.getUniqueId())).findFirst().get();
    }

    public static void addPlayer(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkArgument(!isAlreadyRegistered(practicePlayer), "practicePlayer is already registered");

        getPracticePlayers().add(practicePlayer);
    }

    public static void removePlayer(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkArgument(isAlreadyRegistered(practicePlayer), "practicePlayer is not registered");

        getPracticePlayers().remove(practicePlayer);
    }

}
