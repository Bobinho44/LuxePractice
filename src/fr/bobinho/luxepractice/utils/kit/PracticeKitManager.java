package fr.bobinho.luxepractice.utils.kit;

import fr.bobinho.luxepractice.utils.player.PracticePlayer;
import org.atlanmod.commons.Guards;
import org.atlanmod.commons.Preconditions;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class PracticeKitManager {

    public static boolean haveEmptyPracticeKitSlot(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return practicePlayer.getKits().size() < 10;
    }

    public static boolean isAlreadyAnUsedPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");

        return practicePlayer.getKits().stream().anyMatch(kit -> kit.getName().equals(kitName));
    }

    public static void createPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(!isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "kitName is already used");
        Guards.checkArgument(haveEmptyPracticeKitSlot(practicePlayer), "practicePlayer have already 10 kits");

        practicePlayer.addKit(new PracticeKit(kitName, getPlayerInventoryAsKit(practicePlayer)));
    }

    public static void deletePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        PracticeKit kit = practicePlayer.getKit(kitName).get();
        if (practicePlayer.getAutoKit().equals(kit)) {
            removeAutoPracticeKit(practicePlayer);
        }
        practicePlayer.removeKit(kit);

    }

    private static ItemStack[] getPlayerInventoryAsKit(@Nonnull PracticePlayer practicePlayer) {
        Preconditions.requireThat(practicePlayer).isNotNull();
        Preconditions.requireThat(practicePlayer.getSpigotPlayer().isPresent()).isTrue();

        ItemStack[] items = new ItemStack[36];
        for (int i = 0; i < 36; i++) {
            items[i] = practicePlayer.getSpigotPlayer().get().getInventory().getItem(i).clone();
        }
        return items;
    }

    public static void givePracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        for (int i = 0; i < 36; i++) {
            practicePlayer.getSpigotPlayer().get().getInventory().setItem(i, practicePlayer.getKit(kitName).get().getItem(i));
        }
    }

    public static boolean haveAutoPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");

        return practicePlayer.getAutoKit() != null;
    }

    public static void setAutoPracticeKit(@Nonnull PracticePlayer practicePlayer, @Nonnull String kitName) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkNotNull(kitName, "kitName is null");
        Guards.checkArgument(!haveAutoPracticeKit(practicePlayer), "practicePlayer already have an auto kit");
        Guards.checkArgument(isAlreadyAnUsedPracticeKit(practicePlayer, kitName), "this kit doesn't exist");

        practicePlayer.setAutoKit(practicePlayer.getKit(kitName).get());
    }

    public static void removeAutoPracticeKit(@Nonnull PracticePlayer practicePlayer) {
        Guards.checkNotNull(practicePlayer, "practicePlayer is null");
        Guards.checkArgument(haveAutoPracticeKit(practicePlayer), "practicePlayer doesn't have an auto kit");

        practicePlayer.setAutoKit(null);
    }

}
