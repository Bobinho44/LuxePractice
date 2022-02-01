package fr.bobinho.luxepractice.utils.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PracticeLocationUtil {

    public static String getAsString(Location location) {
        return String.valueOf(location.getWorld().getName()) + ":" +
                location.getX() + ":" +
                location.getY() + ":" +
                location.getZ() + ":" +
                location.getYaw() + ":" +
                location.getPitch();
    }

    public static Location getAsLocation(String locationString) {
        String[] locationInformations = locationString.split(":");
        return new Location(
                Bukkit.getWorld(locationInformations[0]),
                Double.parseDouble(locationInformations[1]),
                Double.parseDouble(locationInformations[2]),
                Double.parseDouble(locationInformations[3]),
                Float.parseFloat(locationInformations[4]),
                Float.parseFloat(locationInformations[5])
        );
    }

}
