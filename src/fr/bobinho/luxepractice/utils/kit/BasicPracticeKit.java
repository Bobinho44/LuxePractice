package fr.bobinho.luxepractice.utils.kit;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BasicPracticeKit extends PracticeKit {

    /**
     * Fields
     */
    private boolean isDefaultKit;

    /**
     * Creates a new basic practice kit
     *
     * @param name
     * @param items
     * @param isDefaultKit
     */
    public BasicPracticeKit(@NotNull String name, @NotNull ItemStack[] items, boolean isDefaultKit) {
        super(name, items);
        setIsDefaultKit(isDefaultKit);
    }

    /**
     * Checks if the basic practice kit is the default
     *
     * @return if it is the default basic practice kit
     */
    public boolean isDefaultKit() {
        return isDefaultKit;
    }

    /**
     * Sets the default statue of a basic practice kit
     *
     * @param isDefaultKit the new default statue of the basic practice kit
     */
    protected void setIsDefaultKit(boolean isDefaultKit) {
        this.isDefaultKit = isDefaultKit;
    }

}
