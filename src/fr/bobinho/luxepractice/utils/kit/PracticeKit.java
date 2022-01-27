package fr.bobinho.luxepractice.utils.kit;

import org.atlanmod.commons.Guards;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class PracticeKit {

    String name;
    ItemStack[] items;

    public PracticeKit(@Nonnull String name, @Nonnull ItemStack[] items) {
        this.name = name;
        this.items = items;
    }

    public String getName() {
        return name;
    }

    private ItemStack[] getItems() {
        return items;
    }

    public ItemStack getItem(int i) {
        Guards.checkArgument(i >= 0 && i < 40, "the slot " + i + " doesn't exist");

        return getItems()[i];
    }

}
