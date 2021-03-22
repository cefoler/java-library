package com.celeste.util.date;

import com.celeste.annotation.Utility;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Utility
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

    @Getter
    private static final DateUtil instance = new DateUtil();

    private final SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm");

    /**
     * Converts the timestamp into a String of the date.
     *
     * @param timestamp Long
     * @return String
     */
    public String convertFrom(final long timestamp) {
        format.setTimeZone(TimeZone.getDefault());
        return format.format(timestamp);
    }

    /**
     * Converts the string into long.
     *
     * @param string Date
     * @return Long
     */
    public Long convertTo(final String string) {
        final String formatted = string.toUpperCase();
        final long time = Long.parseLong(formatted.substring(0, formatted.length() - 1));

        for (TimeMultiplier value : TimeMultiplier.values()) {
            if (!formatted.endsWith(String.valueOf(value.name().charAt(0)))) continue;
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
    public Long convert(final String string) {
        long time = 0;
        final String[] split = string.split(" ");

        for (String splittedString : split) {
            final char charAt = splittedString.charAt(splittedString.length() - 1);

            int timeInt;
            try {
                timeInt = Integer.parseInt(splittedString.replace(Character.toString(charAt), ""));
            } catch (NumberFormatException exception) {
                return 0L;
            }

            for (TimeMultiplier timeMultiplier : TimeMultiplier.values()) {
                if (timeMultiplier.getDiminutive() != charAt) continue;
                time += timeMultiplier.getMultiplier() * timeInt;
            }
        }

        return time;
    }

}
