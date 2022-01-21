package fr.bobinho.luxepractice.utils.settings;

import fr.bobinho.luxepractice.LuxePracticeCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Settings file.
 */
public class PracticeSettings {

    private static YamlConfiguration configuration;

    /**
     * Initializes settings file.
     */
    public static void Initialize() {
        File file = new File(LuxePracticeCore.getInstance().getDataFolder() + "/settings.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (InputStream input = PracticeSettings.class.getResourceAsStream("/settings.yml")) {
                if (input != null)
                    Files.copy(input, file.toPath());
                else
                    file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);

        Location initialSpawn = new Location(Bukkit.getServer().getWorlds().get(0), 0, 0, 0, 0 ,0);
        configuration.set("spawn", initialSpawn);

        //Material.valueOf("ENDER_PEARL");
        configuration.set("cooldown.ENDER_PEARL", 10);

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Successfully loaded settings");
    }

    /**
     * Gets configuration.
     *
     * @return Configuration.
     */
    @Nonnull
    public static YamlConfiguration getConfiguration() {
        return configuration;
    }

}
