package fr.bobinho.luxepractice.utils.kit;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PracticeKit {

    /**
     * Fields
     */
    private final String name;
    private final ItemStack[] items;

    /**
     * Creates a new practice kit
     *
     * @param name  the practice kit name
     * @param items the practice kit items
     */
    public PracticeKit(@Nonnull String name, @Nonnull ItemStack[] items) {
        Validate.notNull(name, "name is null");
        Validate.notNull(items, "items is null");

        this.name = name;
        this.items = items;
    }

    /**
     * Gets the practice kit name
     *
     * @return the practice kit name
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets the practice kit items
     *
     * @return the practice kit items
     */
    @Nonnull
    private ItemStack[] getItems() {
        return items;
    }

    /**
     * Gets the item from slot "slot" of the practice kit items
     *
     * @param slot the slot
     * @return the item from slot "slot" of the practice kit items
     */
    @Nullable
    public ItemStack getItem(int slot) {
        Validate.isTrue(slot >= 0 && slot < 41, "slot is invalid");

        return getItems()[slot];
    }

}
