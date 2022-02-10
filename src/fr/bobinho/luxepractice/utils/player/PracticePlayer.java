package fr.bobinho.luxepractice.utils.player;

import com.nametagedit.plugin.NametagEdit;
import com.nametagedit.plugin.api.data.Nametag;
import fr.bobinho.luxepractice.LuxePracticeCore;
import fr.bobinho.luxepractice.utils.format.PracticeNumberFormat;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class PracticePlayer {

    /**
     * Fields
     */
    private final UUID uuid;
    private final String name;
    private int kills;
    private int deaths;
    private final List<PracticeKit> kits = new ArrayList<>();
    private PracticeKit autoKit;
    private ItemStack[] oldInventory = new ItemStack[41];
    private String[] nametag = new String[2];

    /**
     * Creates a new practice player
     *
     * @param uuid   the practice player uuid
     * @param name   the practice player name
     * @param kills  the practice player kills number
     * @param deaths the practice player deaths number
     */
    public PracticePlayer(@Nonnull UUID uuid, @Nonnull String name, int kills, int deaths) {
        Validate.notNull(uuid, "uuid is null");
        Validate.notNull(name, "name is null");
        Validate.isTrue(kills >= 0, "kills is negative");
        Validate.isTrue(deaths >= 0, "deaths is negative");

        this.uuid = uuid;
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
    }

    /**
     * Creates a new practice player
     *
     * @param player the player
     */
    public PracticePlayer(@Nonnull Player player) {
        this(player.getUniqueId(), player.getName(), 0, 0);
    }

    /**
     * Gets the uuid
     *
     * @return the uuid
     */
    @Nonnull
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Gets the spigot player
     *
     * @return the spigot player
     */
    public Player getSpigotPlayer() {
        return Bukkit.getPlayer(getUuid());
    }

    /**
     * Gets the practice player name
     *
     * @return the name
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets the practice player deaths number
     *
     * @return the deaths number
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Add 1 death to the practice player
     */
    public void addDeath() {
        deaths++;
    }

    /**
     * Gets the practice player kills number
     *
     * @return the kills number
     */
    public int getKills() {
        return kills;
    }

    /**
     * Add 1 kill to the practice player
     */
    public void addKill() {
        kills++;
    }

    /**
     * Gets practice the player ratio
     *
     * @return the ratio
     */
    public float getRatio() {
        return PracticeNumberFormat.getAsTwoDecimalsFormat((float) getKills() / (getDeaths() == 0 ? 1 : getDeaths()));
    }

    /**
     * Gets the practice player kits
     *
     * @return all kits
     */
    @Nonnull
    public List<PracticeKit> getKits() {
        return kits;
    }

    /**
     * Gets the practice player kit named "kitname"
     *
     * @param kitName the kit name
     * @return the find kit
     */
    @Nonnull
    public Optional<PracticeKit> getKit(String kitName) {
        Validate.notNull(kitName, "kitname is null");

        return getKits().stream().filter(kit -> kit.getName().equalsIgnoreCase(kitName)).findFirst();
    }

    /**
     * Adds a practice player kit
     *
     * @param kit the kit
     */
    public void addKit(PracticeKit kit) {
        Validate.notNull(kit, "kit is null");

        getKits().add(kit);
    }

    /**
     * Removes a practice player kit
     *
     * @param kit the kit
     */
    public void removeKit(PracticeKit kit) {
        Validate.notNull(kit, "kit is null");

        getKits().remove(kit);
    }

    /**
     * Gets the practice player auto kit
     *
     * @return the kit
     */
    @Nonnull
    public Optional<PracticeKit> getAutoKit() {
        return Optional.ofNullable(autoKit);
    }

    /**
     * Sets the practice player auto kit
     *
     * @param autoKit the kit
     */
    public void setAutoKit(@Nullable PracticeKit autoKit) {
        this.autoKit = autoKit;
    }

    /**
     * Gets the old practice player inventory
     *
     * @return the old inventory
     */
    @Nonnull
    public ItemStack[] getOldInventory() {
        return oldInventory;
    }

    /**
     * Saves the practice player inventory
     */
    public void saveOldInventory() {
        oldInventory = PracticeKitManager.getPracticePlayerInventoryAsKit(this);
    }

    /**
     * Saves the practice player inventory
     *
     * @param items the inventory items
     */
    public void saveOldInventory(@Nonnull ItemStack[] items) {
        Validate.notNull(items, "items is null");

        oldInventory = items;
    }

    /**
     * Gets the practice player nametag
     * @return the practice player nametag
     */
    @Nullable
    public String[] getNametag() {
        return nametag;
    }

    /**
     * Sets the practice player nametag
     * @param nametag the practice player nametag
     */
    public void setNametag(String[] nametag) {
        this.nametag = nametag;
    }

    /**
     * Gets the item from slot "slot" of the practice player inventory
     *
     * @param slot the inventory slot
     * @return the item
     */
    @Nullable
    public ItemStack getItem(int slot) {
        ItemStack item = getSpigotPlayer().getInventory().getItem(slot);
        return item == null ? null : item.clone();
    }

    /**
     * Sets the item from slot "slot" of the practice player inventory
     *
     * @param slot the inventory slot
     * @param item the item
     */
    public void setItem(int slot, @Nullable ItemStack item) {
        getSpigotPlayer().getInventory().setItem(slot, item);
    }

    /**
     * Opens an inventory to the practice player
     *
     * @param inventory the inventory
     */
    public void openInventory(@Nonnull Inventory inventory) {
        Validate.notNull(inventory, "inventory is null");

        getSpigotPlayer().openInventory(inventory);
    }

    /**
     * Teleports the practice player in a radius of 10 blocks around the location
     *
     * @param location the location
     */
    public void teleport(@Nonnull Location location) {
        Validate.notNull(location, "location is null");

        //Teleports the player to the location
        getSpigotPlayer().teleport(location);
    }

    /**
     * Teleports the practice player to a world spawn
     */
    public void teleportToTheSpawn(@Nonnull String worldType) {
        Validate.notNull(worldType, "worldType is null");

        //Gets and teleports the player to the spawn
        Location spawn = PracticeLocationUtil.getAsLocation(LuxePracticeCore.getMainSettings().getConfiguration().getString("spawn." + worldType, worldType + ":0:1000:0:0:0"));
        if (spawn.getY() >= 1000) {
            spawn.setY(spawn.getWorld().getHighestBlockYAt(spawn.getBlockX(), spawn.getBlockZ()) + 1);
        }
        getSpigotPlayer().teleport(spawn);

        //Sends the message
        sendMessage(ChatColor.GREEN + "Teleporting to spawn...");
    }

    /**
     * Teleports the practice player to the overworld spawn
     */
    public void teleportToTheSpawn() {
        teleportToTheSpawn("world");
    }

    /**
     * Gets the practice player health information
     *
     * @return the practice player health information
     */
    @Nonnull
    public String getHealthInformation() {
        return ChatColor.GOLD + "Health: " + ChatColor.RED + getSpigotPlayer().getHealth();
    }

    /**
     * Gets the practice player active potion effects information
     *
     * @return the practice player active potion effects information
     */
    public List<String> getActivePotionEffectsInformation() {
        return getSpigotPlayer().getActivePotionEffects().stream().map(potion -> ChatColor.YELLOW + potion.getType().getName().replace("_", " ")).collect(Collectors.toList());
    }

    /**
     * Removes the practice player potion effects
     */
    public void removeAllPotionEffects() {
        for (PotionEffect effect : getSpigotPlayer().getActivePotionEffects()) {
            getSpigotPlayer().removePotionEffect(effect.getType());
        }
    }

    /**
     * Gives a potion effect to the practice player
     *
     * @param potionEffect the potion effect
     */
    public void addPotionEffect(@Nonnull PotionEffect potionEffect) {
        Validate.notNull(potionEffect, "potionEffect is null");

        getSpigotPlayer().addPotionEffect(potionEffect);
    }

    /**
     * Gives or removes the flight permission to the practice player
     *
     * @param allowFlight the allow flight statue
     */
    public void setAllowFlight(boolean allowFlight) {
        getSpigotPlayer().setAllowFlight(allowFlight);
    }


    /**
     * Gets the clickable text to open the practice player inventory
     *
     * @return the clickable text
     */
    @Nonnull
    public TextComponent getClickableInventoryAccessAsString() {
        TextComponent clickableInventoryAccess = new TextComponent(getName());
        clickableInventoryAccess.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/practiceinventory Mm7kTCD2 " + getName()));
        return clickableInventoryAccess;
    }

    /**
     * Sends a message to the practice player
     *
     * @param message the message
     */
    public void sendMessage(BaseComponent[] message) {
        getSpigotPlayer().sendMessage(message);
    }

    /**
     * Sends a message to the practice player
     *
     * @param message the message
     */
    public void sendMessage(TextComponent message) {
        getSpigotPlayer().sendMessage(message);
    }

    /**
     * Sends a message to the practice player
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        getSpigotPlayer().sendMessage(message);
    }

    /**
     * Sets the practice player name tag
     *
     * @param color the practice team color
     */
    public void changeName(@Nullable ChatColor color) {
        if (color == null) {
            NametagEdit.getApi().setNametag(getSpigotPlayer(), Objects.requireNonNull(getNametag())[0], getNametag()[1]);
        }
        else {
            Nametag nametag = NametagEdit.getApi().getNametag(getSpigotPlayer());
            setNametag(new String[]{nametag.getPrefix(), nametag.getSuffix()});
            NametagEdit.getApi().setNametag(getSpigotPlayer(), color + "", "");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PracticePlayer)) {
            return false;
        }

        PracticePlayer testedPracticePlayer = (PracticePlayer) obj;
        return testedPracticePlayer.getUuid().equals(getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
