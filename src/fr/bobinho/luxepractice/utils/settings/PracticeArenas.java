package fr.bobinho.luxepractice.utils.settings;

import fr.bobinho.luxepractice.LuxePracticeCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import javax.annotation.Nonnull;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

public class PracticeArenas {

    private static YamlConfiguration configuration;

    /**
     * Initializes arenas file
     */
    public static void Initialize() {
        File file = new File(LuxePracticeCore.getInstance().getDataFolder() + "/arenas.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (InputStream input = PracticeSettings.class.getResourceAsStream("/arenas.yml")) {
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

        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Successfully loaded arenas data");
    }

    /**
     * Gets configuration
     *
     * @return Configuration
     */
    @Nonnull
    public static YamlConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Clears configuration
     */
    public static void clear() {
        for (String key : getConfiguration().getKeys(false)) {
            getConfiguration().set(key, null);
        }
    }

}