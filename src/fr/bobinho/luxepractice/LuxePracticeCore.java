package fr.bobinho.luxepractice;

import co.aikar.commands.PaperCommandManager;
import fr.bobinho.luxepractice.commands.arena.*;
import fr.bobinho.luxepractice.commands.chest.DelChestCommand;
import fr.bobinho.luxepractice.commands.chest.SetChestCommand;
import fr.bobinho.luxepractice.commands.inventory.PracticeInventoryCommand;
import fr.bobinho.luxepractice.commands.kit.*;
import fr.bobinho.luxepractice.commands.spawn.SetSpawnCommand;
import fr.bobinho.luxepractice.commands.spawn.SpawnCommand;
import fr.bobinho.luxepractice.commands.spectator.SpectateCommand;
import fr.bobinho.luxepractice.commands.stats.StatsCommand;
import fr.bobinho.luxepractice.commands.team.*;
import fr.bobinho.luxepractice.listeners.*;
import fr.bobinho.luxepractice.utils.arena.PracticeArenaManager;
import fr.bobinho.luxepractice.utils.kit.PracticeKitManager;
import fr.bobinho.luxepractice.utils.player.PracticePlayerManager;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;

public class LuxePracticeCore extends JavaPlugin {

    /**
     * Fields
     */
    private static LuxePracticeCore instance;
    private static PracticeSettings arenasSettings;
    private static PracticeSettings playersSettings;
    private static PracticeSettings mainSettings;

    /**
     * Gets the luxe practice core instance
     *
     * @return the luxe practice core instance
     */
    @Nonnull
    public static LuxePracticeCore getInstance() {
        return instance;
    }

    /**
     * Gets the arenas settings
     *
     * @return the arenas settings
     */
    @Nonnull
    public static PracticeSettings getArenasSettings() {
        return arenasSettings;
    }

    /**
     * Gets the players settings
     *
     * @return the players settings
     */
    @Nonnull
    public static PracticeSettings getPlayersSettings() {
        return playersSettings;
    }

    /**
     * Gets the main settings
     *
     * @return the main settings
     */
    @Nonnull
    public static PracticeSettings getMainSettings() {
        return mainSettings;
    }

    /**
     * Enable and initialize the plugin
     */
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LuxePractice] Loading the plugin...");

        registerCommands();
        registerListeners();

        arenasSettings = new PracticeSettings("arenas");
        playersSettings = new PracticeSettings("players");
        mainSettings = new PracticeSettings("settings");

        PracticeArenaManager.loadPracticeArenasData();
        PracticeKitManager.loadBasicPracticeKits();
    }

    /**
     * Disable the plugin and save data
     */
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LuxePractice] Unloading the plugin...");

        PracticeArenaManager.savePracticeArenasData();
        PracticeKitManager.saveBasicPracticeKits();
        Bukkit.getOnlinePlayers().forEach(player -> PracticePlayerManager.savePracticePlayerData(player.getUniqueId()));
    }

    /**
     * Register listeners
     */
    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new CooldownListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DropListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BuildListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ArenaListener(), this);

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Successfully loaded listeners");
    }

    /**
     * Register commands
     */
    private void registerCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);

        //Registers spawn's command
        commandManager.registerCommand(new SetSpawnCommand());
        commandManager.registerCommand(new SpawnCommand());

        //Registers stats' command
        commandManager.registerCommand(new StatsCommand());

        //Registers chest's command
        commandManager.registerCommand(new SetChestCommand());
        commandManager.registerCommand(new DelChestCommand());

        //Registers kit's command
        commandManager.registerCommand(new SaveKitCommand());
        commandManager.registerCommand(new DelKitCommand());
        commandManager.registerCommand(new LoadKitCommand());
        commandManager.registerCommand(new AutoKitCommand());
        commandManager.registerCommand(new SetKitCommand());
        commandManager.registerCommand(new UnsetKitCommand());

        //Register see inventory's command
        commandManager.registerCommand(new PracticeInventoryCommand());

        //Registers arena's command
        commandManager.registerCommand(new MatchCommand());
        commandManager.registerCommand(new DuelCommand());
        commandManager.registerCommand(new DelArenaCommand());
        commandManager.registerCommand(new SetArenaCommand());

        //Registers spectator's command
        commandManager.registerCommand(new SpectateCommand());

        //Registers team's command
        commandManager.registerCommand(new CreateTeamCommand());
        commandManager.registerCommand(new DisbandTeamCommand());
        commandManager.registerCommand(new LeaveTeamCommand());
        commandManager.registerCommand(new TeamCommand());
        commandManager.registerCommand(new TeamInviteCommand());
        commandManager.registerCommand(new TeamListCommand());
        commandManager.registerCommand(new TeamDuelCommand());

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Successfully loaded commands");
    }

}
