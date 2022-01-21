package fr.bobinho.luxepractice.utils.player;

import fr.bobinho.luxepractice.utils.kit.PracticeKit;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import org.atlanmod.commons.Guards;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PracticePlayer {

    private final UUID uuid;
    private Player player;
    private String name;
    private int kills;
    private int deaths;
    private List<PracticeKit> kits;

    public PracticePlayer(UUID uuid, String name, int kills, int deaths, List<PracticeKit> kits) {
        this.uuid = uuid;
        this.name = name;
        this.kills = kills;
        this.deaths = deaths;
        this.kits = kits;
    }

    public PracticePlayer (Player player) {
        this(player.getUniqueId(), player.getName(), 0, 0, new ArrayList<PracticeKit>());
    }

    public PracticePlayer (Player player, int kills, int deaths) {
        this(player.getUniqueId(), player.getName(), kills, deaths, new ArrayList<PracticeKit>());
    }

    public PracticePlayer (UUID uuid, String name) {
        this(uuid, name, 0, 0, new ArrayList<PracticeKit>());
    }

    public UUID getUuid() {
        return uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDeaths() {
        return deaths;
    }

    public void addDeath() {
        deaths++;
    }

    public int getKills() {
        return kills;
    }

    public void addKill() {
        kills++;
    }

    public float getRatio() {
        return (float) getKills() / getDeaths();
    }

    private List<PracticeKit> getKits() {
        return kits;
    }

    public Optional<PracticeKit> getKit(String kitName) {
        return getKits().stream().filter(kit -> kit.getName().equals(kitName)).findFirst();
    }

    public boolean isItPracticeKit(String kitName) {
        return getKits().stream().anyMatch(kit -> kit.getName().equals(kitName));
    }

    public boolean canAddKit() {
        return getKits().size() < 10;
    }

    public void createKit(String kitName) {
        Guards.checkArgument(canAddKit(), "there are already 10 kits");
        Guards.checkArgument(!isItPracticeKit(kitName), "the kit name " + kitName + " is already used");

        getKits().add(new PracticeKit(kitName, PracticeKitManager.getPlayerInventoryAsKit(getPlayer())));
    }

    public void deletekit(String kitName) {
        Guards.checkArgument(isItPracticeKit(kitName), "the kit " + kitName + " doesn't exist");

        getKits().remove(getKit(kitName));
    }
}
