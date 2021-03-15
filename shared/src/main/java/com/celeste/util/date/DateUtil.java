package com.celeste.util.date;

import com.celeste.annotation.Utility;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Utility
public final class DateUtil {

    private DateUtil() {}

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    /**
     * Converts the timestamp into a String of the date.
     *
     * @param timestamp Long
     * @return String
     */
    public static String convertToString(final long timestamp) {
        SIMPLE_DATE_FORMAT.setTimeZone(TimeZone.getDefault());
        return SIMPLE_DATE_FORMAT.format(timestamp);
    }

    /**
     * Converts the string into long.
     *
     * @param string Date
     * @return Long
     */
    public static Long convertToTimestamp(final String string) {
        final String formatted = string.toUpperCase();
        long time = Long.parseLong(formatted.substring(0, formatted.length() - 1));

        for (TimeMultiplier value : TimeMultiplier.values()) {
            if (!formatted.endsWith(String.valueOf(value.name().charAt(0)))) {
                continue;
            }

            return time * value.getMultiplier();
        }

        return null;
    }

    /**
     * Converts date (1d, 1s, 1m) into long.
     *
     * @param string Date
     * @return Long
     */
    public static Long convert(final String string) {
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
                if (timeMultiplier.getDiminutive() != charAt) {
                    continue;
                }

                time += timeMultiplier.getMultiplier() * timeInt;
            }
        }

        return time;
    }

}
