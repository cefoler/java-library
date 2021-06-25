package com.celeste.library.core.util.formatter;

import com.celeste.library.core.util.formatter.type.TimeType;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.celeste.library.core.util.pattern.RegexPattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateFormatter {

  private static final SimpleDateFormat DATE_FORMAT;

  static {
    DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    DATE_FORMAT.setTimeZone(TimeZone.getDefault());
  }

  /**
   * Converts the timestamp into a String of the date.
   *
   * @param time Long
   * @return String
   */
  public static String convertToString(final long time) {
    return DATE_FORMAT.format(time);
  }

  /**
   * Transforms the date into the format
   *
   * @param date Date
   * @return String
   */
  public static String convertToString(final Date date) {
    return DATE_FORMAT.format(date.getTime());
  }

  /**
   * Converts date (1d, 1s, 1m) into long.
   *
   * @param dates Date
   * @return Long
   */
  public static long convert(final String... dates) {
    long totalTime = 0;

    for (final String date : dates) {
      final String prefix = date.replaceAll(RegexPattern.NUMBERS.getPattern(), "");
      final int time = Integer.parseInt(date.replaceAll(RegexPattern.LETTERS.getPattern(), ""));

      final TimeType type = TimeType.getType(prefix);
      totalTime += type.getMultiplier() * time;
    }

    return totalTime;
  }

  /**
   * Formats the time to the format 7 days, 7 seconds, etc.
   *
   * @param time long
   * @return String
   */
  public static String format(final long time) {
    final long differenceTime = System.currentTimeMillis() - time;

    final long days = TimeUnit.MILLISECONDS.toDays(differenceTime);
    final long hours = TimeUnit.MILLISECONDS.toHours(differenceTime)
        - TimeUnit.MILLISECONDS.toDays(differenceTime) * 24;
    final long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceTime)
        - TimeUnit.MILLISECONDS.toHours(differenceTime) * 60;
    final long seconds = TimeUnit.MILLISECONDS.toSeconds(differenceTime)
        - TimeUnit.MILLISECONDS.toMinutes(differenceTime) * 60;

    final StringBuilder builder = new StringBuilder();

    if (days > 0) {
      builder.append(days);
      builder.append(days == 1 ? " day" : "days");
    }

    if (hours > 0) {
      builder.append(days > 0 && (minutes > 0 || seconds > 0) ? ", " : " and ");
      builder.append(hours);
      builder.append(hours == 1 ? "hour" : "hours");
    }

    if (minutes > 0) {
      builder.append(days > 0 || hours > 0 && (seconds > 0) ? ", " : " and ");
      builder.append(minutes);
      builder.append(minutes == 1 ? " minute" : " minutes");
    }

    if (seconds > 0) {
      builder.append(days > 0 || hours > 0 || minutes > 0 ? " and " : builder.length() > 0 ? ", " : "");
      builder.append(seconds);
      builder.append(seconds == 1 ? " second" : "seconds");
    }

    return builder.toString();
  }

}
