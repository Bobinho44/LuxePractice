package fr.bobinho.luxepractice.utils.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nonnull;
import java.util.Objects;

public class PracticeLocationUtil {

    /**
     * Serializes a location
     *
     * @param location the location
     * @return the serialized location
     */
    @Nonnull
    public static String getAsString(@Nonnull Location location) {
        Objects.requireNonNull(location, "location is null");

        return location.getWorld().getName() + ":" +
                location.getX() + ":" +
                location.getY() + ":" +
                location.getZ() + ":" +
                location.getYaw() + ":" +
                location.getPitch();
    }

    /**
     * Deserializes a location
     *
     * @param locationString the location string
     * @return the deserialized location
     */
    @Nonnull
    public static Location getAsLocation(@Nonnull String locationString) {
        Objects.requireNonNull(locationString, "locationString is null");

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
