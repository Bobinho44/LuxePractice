package fr.bobinho.luxepractice.utils.player;

import com.mojang.authlib.GameProfile;
import fr.bobinho.luxepractice.utils.arena.request.PracticeRequest;
import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.location.PracticeLocationUtil;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import org.atlanmod.commons.Guards;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

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
    private ItemStack[] oldInventory;

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
     * Teleports the player in a radius of 10 blocks around the location
     * @param location the location
     */
    public void teleportAroundLocation(@Nonnull Location location) {
        Guards.checkNotNull(location, "location is null");

        //Teleports the player around the location (radius of 10 blocks)
        Random random = new Random();
        getSpigotPlayer().get().teleport(location.clone().add(random.nextInt(10), 1, random.nextInt(10)).toHighestLocation());
    }

    /**
     * Removes the player potion effects
     */
    public void removeAllPotionEffects() {
        for (PotionEffect effect : getSpigotPlayer().get().getActivePotionEffects()) {
            getSpigotPlayer().get().removePotionEffect(effect.getType());
        }
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

        return getKits().stream().filter(kit -> kit.getName().equalsIgnoreCase(kitName)).findFirst();
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
        clickableInventoryAccess.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/practiceinventory " + getName()));
        return clickableInventoryAccess;
    }

    public void sendMessage(BaseComponent[] message) {
        getSpigotPlayer().get().sendMessage(message);
    }

    public void sendMessage(String message) {
        getSpigotPlayer().get().sendMessage(message);
    }

    public void addPotionEffect(PotionEffect potionEffect) {
        getSpigotPlayer().get().addPotionEffect(potionEffect);
    }

    public void setAllowFlight(boolean allowFlight) {
        getSpigotPlayer().get().setAllowFlight(allowFlight);
    }

    public ItemStack getItem(int slot) {
        ItemStack item = getSpigotPlayer().get().getInventory().getItem(slot);
        return item == null ? null : item.clone();
    }

    public void setItem(int slot, ItemStack item) {
        getSpigotPlayer().get().getInventory().setItem(slot, item);
    }

    public void teleportToTheSpawn() {
        //Gets and teleports the player to the spawn
        Location spawn = PracticeLocationUtil.getAsLocation(PracticeSettings.getConfiguration().getString("spawn"));
        getSpigotPlayer().get().teleport(spawn);

        //Sends the message
        sendMessage(ChatColor.GREEN + "Teleportation to the spawn...");
        PracticeKitManager.givePracticeKit(this, new PracticeKit("oldInventory", getOldInventory()));
    }

    public   ItemStack[] getOldInventory() {
        return oldInventory;
    }

    public void saveOldInventory() {
        oldInventory = PracticeKitManager.getPlayerInventoryAsKit(this);
    }

    public void saveOldInventory(ItemStack[] items) {
        oldInventory = items;
    }

    public void changeName(String name){
        Player viewer = getSpigotPlayer().get();
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player == viewer) continue ;
            // SUPPRIME LE JOUEUR
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer)viewer).getHandle()));
            // MODIFIE LE PROFIL DE JEU DU JOUEUR
            GameProfile gp = ((CraftPlayer)viewer).getProfile();
            try {
                Field nameField = GameProfile.class.getDeclaredField("name");
                nameField.setAccessible(true);

                nameField.set(gp, name);
            } catch (IllegalAccessException | NoSuchFieldException ex) {
                throw new IllegalStateException(ex);
            }
            //AJOUTE LE JOUEUR
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer)viewer).getHandle()));
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(viewer.getEntityId()));
            ((CraftPlayer)player).getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(((CraftPlayer)viewer).getHandle()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PracticePlayer)) {
            return false;
        }
        PracticePlayer testedPracticePlayer = (PracticePlayer) o;
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
