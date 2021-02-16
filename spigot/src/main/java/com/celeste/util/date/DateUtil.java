package com.celeste.util.date;

import java.text.SimpleDateFormat;

public class DateUtil {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    public static String convertToString(long timestamp) {
        return SIMPLE_DATE_FORMAT.format(timestamp);
    }

    public static Long convertToTimestamp(String string) {
        string = string.toUpperCase();

        long time = Long.parseLong(string.substring(0, string.length() - 1));

        for (TimeMultiplier value : TimeMultiplier.values()) {
            if (!string.endsWith(String.valueOf(value.name().charAt(0)))) continue;

            return time * value.getMultiplier();
        }

        return null;
    }

    public static Long convert(String string) {
        long time = 0;
        String[] split = string.split(" ");

        for (String splittedString : split) {
            char charAt = splittedString.charAt(splittedString.length() - 1);

            int timeInt;
            try {
                timeInt = Integer.parseInt(splittedString.replace(Character.toString(charAt), ""));
            } catch (NumberFormatException exception) {
                return (long) -1;
            }

            for (TimeMultiplier timeMultiplier : TimeMultiplier.values()) {
                if (timeMultiplier.getDiminutive() != charAt) continue;

                time += timeMultiplier.getMultiplier() * timeInt;
            }
        }

        return time;
    }

}