package fr.bobinho.luxepractice.utils.kit;

import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

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
        Objects.requireNonNull(name, "name is null");
        Objects.requireNonNull(items, "items is null");

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
        return getItems()[slot];
    }

}
