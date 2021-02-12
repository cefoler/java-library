package com.celeste.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {

    public static String serialize(Location location) {
        return location.getWorld().getName() + ";" + location.getX() + ";"
            + location.getY() + ";" + location.getZ() + ";" + location.getYaw() + ";" + location.getPitch() ;
    }

    public static Location deserialize(String s) {
        String[] stripped = s.split(";");
        if (stripped.length != 4) return null;

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