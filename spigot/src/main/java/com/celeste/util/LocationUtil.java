package com.celeste.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public final class LocationUtil {

    /**
     * Serializes a location into String.
     *
     * @param location Location
     * @return String
     */
    public static String serialize(final Location location) {
        return location.getWorld().getName()
            + ";" + location.getX()
            + ";" + location.getY()
            + ";" + location.getZ()
            + ";" + location.getYaw()
            + ";" + location.getPitch();
    }

    /**
     * Deserializes a String that was serialized by this util.
     * @param context String
     * @return Location
     */
    public static Location deserialize(final String context) {
        final String[] stripped = context.split(";");
        if (stripped.length != 6) return null;

        return new Location(
            Bukkit.getWorld(stripped[0]),
            Double.parseDouble(stripped[1]),
            Double.parseDouble(stripped[2]),
            Double.parseDouble(stripped[3]),
            Float.parseFloat(stripped[4]),
            Float.parseFloat(stripped[5])
        );
    }

}
