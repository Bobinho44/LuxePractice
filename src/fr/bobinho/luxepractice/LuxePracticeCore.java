package fr.bobinho.luxepractice;

import co.aikar.commands.PaperCommandManager;
import fr.bobinho.luxepractice.commands.chest.DelChestCommand;
import fr.bobinho.luxepractice.commands.chest.SetChestCommand;
import fr.bobinho.luxepractice.commands.spawn.SetSpawnCommand;
import fr.bobinho.luxepractice.commands.spawn.SpawnCommand;
import fr.bobinho.luxepractice.commands.StatsCommand;
import fr.bobinho.luxepractice.listeners.BuildListener;
import fr.bobinho.luxepractice.listeners.ChestListener;
import fr.bobinho.luxepractice.listeners.CooldownListener;
import fr.bobinho.luxepractice.listeners.DropListener;
import fr.bobinho.luxepractice.utils.settings.PracticeSettings;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class LuxePracticeCore extends JavaPlugin {

    private static LuxePracticeCore instance;

    public static LuxePracticeCore getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        PracticeSettings.Initialize();

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LuxePractice] Loading the plugin...");
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "[LuxePractice] Unloading the plugin...");
    }

    private void registerListeners() {
        Bukkit.getServer().getPluginManager().registerEvents(new CooldownListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DropListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ChestListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new BuildListener(), this);
    }

    private void registerCommands() {
        final PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new SetSpawnCommand());
        commandManager.registerCommand(new SpawnCommand());
        commandManager.registerCommand(new StatsCommand());
        commandManager.registerCommand(new SetChestCommand());
        commandManager.registerCommand(new DelChestCommand());
    }

}
