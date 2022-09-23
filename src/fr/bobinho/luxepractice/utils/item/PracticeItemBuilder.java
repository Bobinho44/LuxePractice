package fr.bobinho.luxepractice.utils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;

public class PracticeItemBuilder {

    private ItemStack item;
    private ItemMeta meta;

    /**
     * Creates the practice item builder
     *
     * @param item the item stack
     */
    public PracticeItemBuilder(@Nonnull ItemStack item) {
        Objects.requireNonNull(item, "item is null");

        //Configures the bukkit item stack
        this.item = item;
        this.meta = this.item.getItemMeta();
    }

    /**
     * Creates the practice item builder
     *
     * @param material the material
     */
    public PracticeItemBuilder(@Nonnull Material material) {
        Objects.requireNonNull(material, "material is null");

        //Configures the bukkit item stack
        this.item = new ItemStack(material);
        this.meta = this.item.getItemMeta();
    }

    /**
     * Creates the practice item builder
     *
     * @param material the material
     * @param amount   the amount
     */
    public PracticeItemBuilder(@Nonnull Material material, int amount) {
        Objects.requireNonNull(material, "material is null");

        //Configures the bukkit item stack
        this.item = new ItemStack(material, amount);
        this.meta = this.item.getItemMeta();
    }

    /**
     * Creates the practice item builder
     *
     * @param texture the texture
     */
    public PracticeItemBuilder(@Nonnull String texture) {
        Objects.requireNonNull(texture, "texture is null");

        //Configures the bukkit item stack
        this.item = new ItemStack(Material.AIR, 1);
        this.meta = this.item.getItemMeta();

        //Sets the item texture
        texture(texture);
    }

    /**
     * Creates the practice item builder
     *
     * @param player the player for texturing
     */
    public PracticeItemBuilder(@Nonnull Player player) {
        Objects.requireNonNull(player, "player is null");

        //Configures the bukkit item stack
        this.item = new ItemStack(Material.AIR, 1);
        this.meta = this.item.getItemMeta();

        //Sets the item texture
        texture(player);
    }

    /**
     * Gets the current item stack
     *
     * @return the item stack
     */
    @Nonnull
    public ItemStack getItem() {
        return item;
    }

    /**
     * Sets the item stack
     *
     * @param item the item stack
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder item(@Nonnull ItemStack item) {
        Objects.requireNonNull(item, "item is null");

        this.item = item;
        return this;
    }

    /**
     * Sets the item material
     *
     * @param material the material
     * @return the practice item Builder
     */
    @Nonnull
    public PracticeItemBuilder material(@Nonnull Material material) {
        Objects.requireNonNull(material, "material is null");
        item.setType(material);
        return this;
    }

    /**
     * Sets the item durability
     *
     * @param durability the durability
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder durability(int durability) {

        //Checks if the item is not damageable
        if (!(this.meta instanceof Damageable)) {
            return this;
        }

        //Sets item durability.
        ((Damageable) meta).setDamage(durability);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item amount
     *
     * @param amount the amount
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }

    /**
     * Sets the item name
     *
     * @param name the name
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder name(@Nonnull String name) {
        Objects.requireNonNull(name, "name is null");

        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item lore
     *
     * @param lore the lore
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder setLore(@Nonnull List<String> lore) {
        Objects.requireNonNull(lore, "lore is null");

        //Declares the lore
        List<String> lore_array = new ArrayList<>();
        for (@Nonnull String text : lore) {
            lore_array.add(text);
        }

        //Sets the item lore
        meta.setLore(lore_array);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Adds new lines to the lore
     *
     * @param lore the lore
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder lore(@Nonnull List<String> lore) {
        Objects.requireNonNull(lore, "lore is null");

        //Declare the lore array.
        List<String> lore_array = meta.getLore();

        //If the lore is null or empty, create empty list.
        if (lore_array == null) {
            lore_array = new ArrayList<>();
        }

        //Adds new lines to the existed lore
        for (String text : lore) {
            lore_array.add(text);
        }

        //Sets the item lore
        meta.setLore(lore_array);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Adds new lines to the lore
     *
     * @param lore the lore
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder lore(@Nonnull String... lore) {
        Objects.requireNonNull(lore, "lore is null");

        lore(new ArrayList<>(Arrays.asList(lore)));
        return this;
    }

    /**
     * Adds new lines to the lore
     *
     * @param color the color
     * @param lore  the lore
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder lore(@Nonnull String color, @Nonnull List<String> lore) {
        Objects.requireNonNull(color, "color is null");
        Objects.requireNonNull(lore, "lore is null");

        //Declares the colorized lore
        List<String> colorizedLore = new ArrayList<>();
        lore.forEach(text -> colorizedLore.add(color + text));

        //Sets the item lore
        this.lore(colorizedLore);
        return this;
    }

    /**
     * Adds a new enchantment
     *
     * @param enchantment the enchantment
     * @param level       the level
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder enchantment(@Nonnull Enchantment enchantment, int level) {
        Objects.requireNonNull(enchantment, "enchantment is null");

        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Sets the item unbreakable
     *
     * @param unbreakable the unbreakable statue
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder unbreakable(boolean unbreakable) {
        meta.setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Adds an item flag
     *
     * @param flag the flag
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder flag(@Nonnull ItemFlag... flag) {
        Objects.requireNonNull(flag, "flag is null");

        meta.addItemFlags(flag);
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item color
     *
     * @param red   the red
     * @param green the green
     * @param blue  the blue
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder color(int red, int green, int blue) {

        //Checks if item is colorable
        if (!(meta instanceof LeatherArmorMeta)) {
            return this;
        }

        //Colorizes the item
        ((LeatherArmorMeta) meta).setColor(Color.fromRGB(red, green, blue));
        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets the item glow
     *
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder glow() {
        flag(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        enchantment(Enchantment.DURABILITY, 1);
        return this;
    }

    /**
     * Sets the item glow
     *
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder setGlow(boolean glow) {
        if (!glow) {
            return this;
        }
        glow();
        return this;
    }

    /**
     * Sets the item texture
     *
     * @param texture the texture
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder texture(@Nonnull String texture) {
        texture(texture, true);
        return this;
    }

    /**
     * Sets the item texture
     *
     * @param texture the texture
     * @param url     the url statue
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder texture(@Nonnull String texture, boolean url) {
        Objects.requireNonNull(texture, "texture is null");

        //Checks if texture is empty
        if (texture.isEmpty()) {
            return this;
        }

        //Sets the item stack
        if (item.getType() != Material.PLAYER_HEAD) {
            item.setType(Material.PLAYER_HEAD);
        }

        if (meta == null) {
            meta = item.getItemMeta();
        }

        //Configures the url
        if (url) {
            texture = "https://textures.minecraft.net/texture/" + texture;
        }

        //Declares the game profile
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        if (url) {
            byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        } else {
            profile.getProperties().put("textures", new Property("textures", texture));
        }

        //Tries to set game profile to the item
        try {
            Field profile_field = meta.getClass().getDeclaredField("profile");
            profile_field.setAccessible(true);
            profile_field.set(meta, profile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        item.setItemMeta(meta);
        return this;
    }

    /**
     * Sets item texture to player texture
     *
     * @param player the player
     * @return the practice item builder
     */
    @Nonnull
    public PracticeItemBuilder texture(@Nonnull Player player) {
        Objects.requireNonNull(player, "player is null");

        //Gets the game profile
        GameProfile game_profile = ((CraftPlayer) player).getProfile();

        //Checks if texture is empty
        if (!game_profile.getProperties().containsKey("textures")) {

            //Sets the item stack
            item = new ItemStack(Material.PLAYER_HEAD, item.getAmount());
            meta = item.getItemMeta();
            return this;
        }

        String value;
        Iterator<Property> property = game_profile.getProperties().get("textures").iterator();

        if (property.hasNext()) {
            value = property.next().getValue();
        } else {
            value = "";
        }
        texture(value, false);

        return this;
    }

    /**
     * Builds the item
     *
     * @return the item stack
     */
    @Nonnull
    public ItemStack build() {
        return item;
    }

}
