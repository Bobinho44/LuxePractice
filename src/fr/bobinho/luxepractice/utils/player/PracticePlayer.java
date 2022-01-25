package fr.bobinho.luxepractice.utils.player;

import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PracticePlayer {

    /**
     * Fields
     */
    private final UUID uuid;
    private final String name;
    private int kills;
    private int deaths;
    private final List<PracticeKit> kits;
    private PracticeKit autoKit;

    /**
     * Creates a new practice player
     *
     * @param uuid    the player's uuid
     * @param name    the player's name
     * @param kills   the player's kills number
     * @param deaths  the player's deaths number
     * @param kits    the player's kits
     * @param autoKit the player's auto kit
     */
    public PracticePlayer(@Nonnull UUID uuid, @Nonnull String name, int kills, int deaths, @Nonnull List<PracticeKit> kits, @Nonnull PracticeKit autoKit) {
        Guards.checkNotNull(uuid, "uuid is null");
        Guards.checkNotNull(name, "name is null");
        Guards.checkArgument(kills >= 0, "kills is negative");
        Guards.checkArgument(deaths >= 0, "deaths is negative");
        Guards.checkNotNull(kits, "kits is null");

        this.uuid = uuid;
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.kits = kits;
        this.autoKit = autoKit;
    }

    public PracticePlayer(@Nonnull Player player) {
        this(player.getUniqueId(), player.getName(), 0, 0, new ArrayList<PracticeKit>(), null);
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
    @Nonnull
    public Optional<Player> getSpigotPlayer() {
        return Optional.ofNullable(Bukkit.getPlayer(getUuid()));
    }

    /**
     * Gets the player name
     *
     * @return the name
     */
    @Nonnull
    public String getName() {
        return name;
    }

    /**
     * Gets the player deaths number
     *
     * @return the deaths number
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * Add 1 death to the player
     */
    public void addDeath() {
        deaths++;
    }

    /**
     * Gets the player kills number
     *
     * @return the kills number
     */
    public int getKills() {
        return kills;
    }

    /**
     * Add 1 kill  to the player
     */
    public void addKill() {
        kills++;
    }

    /**
     * Gets the player ratio
     *
     * @return the ratio
     */
    public float getRatio() {
        return (float) getKills() / (getDeaths() == 0 ? 1 : getDeaths());
    }

    /**
     * Gets player's kits
     *
     * @return all kits
     */
    @Nonnull
    public List<PracticeKit> getKits() {
        return kits;
    }

    /**
     * Gets the player's kit named "kitname"
     *
     * @param kitName the kit name
     * @return the find kit
     */
    @Nonnull
    public Optional<PracticeKit> getKit(String kitName) {
        Guards.checkNotNull(kitName, "kitname is null");

        return getKits().stream().filter(kit -> kit.getName().equals(kitName)).findFirst();
    }

    /**
     * Adds a player's kit
     *
     * @param kit the kit
     */
    public void addKit(PracticeKit kit) {
        Guards.checkNotNull(kit, "kit is null");

        getKits().add(kit);
    }

    /**
     * Removes a player's kit
     *
     * @param kit the kit
     */
    public void removeKit(PracticeKit kit) {
        Guards.checkNotNull(kit, "kit is null");

        getKits().remove(kit);
    }

    /**
     * Gets the player's auto kit
     *
     * @return the kit
     */
    @Nonnull
    public Optional<PracticeKit> getAutoKit() {
        return Optional.ofNullable(autoKit);
    }

    /**
     * Sets the player's auto kit
     *
     * @param autoKit the kit
     */
    public void setAutoKit(@Nullable PracticeKit autoKit) {
        this.autoKit = autoKit;
    }

    /**
     * Gets the clickable text to open the player's inventory
     * @return the clickable text
     */
    @Nonnull
    public TextComponent getClickableInventoryAccessAsString() {
        TextComponent clickableInventoryAccess = new TextComponent(getName());
        clickableInventoryAccess.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/practiceinventory " + getName() + " " + getUuid()));
        return clickableInventoryAccess;
    }
}
