package fr.bobinho.luxepractice.utils.kit;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DefaultPracticeKit extends PracticeKit {

    private boolean isMainDefaultKit;

    public DefaultPracticeKit(@NotNull String name, @NotNull ItemStack[] items, boolean isMainDefaultKit) {
        super(name, items);
        this.isMainDefaultKit = isMainDefaultKit;
    }

    public boolean isMainDefaultKit() {
        return isMainDefaultKit;
    }

    public void setMainDefaultKit(boolean mainDefaultKit) {
        isMainDefaultKit = mainDefaultKit;
    }

}
