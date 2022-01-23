package fr.bobinho.luxepractice;

import co.aikar.commands.PaperCommandManager;
import fr.bobinho.luxepractice.commands.chest.DelChestCommand;
import fr.bobinho.luxepractice.commands.chest.SetChestCommand;
import fr.bobinho.luxepractice.commands.kit.AutoKitCommand;
import fr.bobinho.luxepractice.commands.kit.DelKitCommand;
import fr.bobinho.luxepractice.commands.kit.LoadKitCommand;
import fr.bobinho.luxepractice.commands.kit.SaveKitCommand;
import fr.bobinho.luxepractice.commands.spawn.SetSpawnCommand;
import fr.bobinho.luxepractice.commands.spawn.SpawnCommand;
import fr.bobinho.luxepractice.commands.StatsCommand;
import fr.bobinho.luxepractice.listeners.*;
import fr.bobinho.luxepractice.utils.settings.PracticeArenas;
import fr.bobinho.luxepractice.utils.settings.PracticePlayers;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class LuxePracticeCore extends JavaPlugin {

    private static LuxePracticeCore instance;

    public static LuxePracticeCore getInstance() {
        return instance;
    }

    /**
     * Enable and initialize the plugin
     */
    public void onEnable() {
        instance = this;

        PracticeSettings.Initialize();
        PracticePlayers.Initialize();
        PracticeArenas.Initialize();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LuxePractice] Loading the plugin...");
    }

    /**
     * Disable the plugin and save data
     */
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LuxePractice] Unloading the plugin...");
        //TODO Save
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
    }

}
