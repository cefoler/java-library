package com.celeste.library.shared.util.date;

import com.celeste.library.shared.util.RegexPattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtil {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");

  /**
   * Converts the timestamp into a String of the date.
   *
   * @param time Long
   * @return String
   */
  @NotNull
  public static String convertToString(final long time) {
    DATE_FORMAT.setTimeZone(TimeZone.getDefault());
    return DATE_FORMAT.format(time);
  }

  /**
   * Transforms the date into the format
   * @param date Date
   * @return String
   */
  @NotNull
  public static String convertToString(@NotNull final Date date) {
    return convertToString(date.getTime());
  }

  /**
   * Converts date (1d, 1s, 1m) into long.
   *
   * @param dates Date
   * @return Long
   */
  public static long convert(@NotNull final String... dates) {
    long totalTime = 0;

    for (final String date : dates) {
      final int time = Integer.parseInt(date.split(RegexPattern.REMOVE_LETTERS)[0]);
      final String prefix = date.split(RegexPattern.REMOVE_NUMBERS)[0];

      final TimeMultiplier type = TimeMultiplier.getType(prefix);
      totalTime += type.getMultiplier() * time;
    }

    if (totalTime == 0) return totalTime;
    throw new NullPointerException("Dates is empty");
  }

  /**
   * Converts date (1d, 1s, 1m) into long.
   *
   * @return Long
   */
  public static long convert(@NotNull final String date) {
    long time = 0;

    final String[] split = date.split(" ");
    for (String arg : split) {
      char prefix = arg.charAt(arg.length() - 1);
      int timeInt = Integer.parseInt(arg.replace(Character.toString(prefix), ""));

      time += TimeMultiplier.getType(prefix).getMultiplier() * timeInt;
    }

    return time;
  }

  /**
   * Formats the time to the format 7 days, 7 seconds, etc.
   * @param time long
   * @return String
   */
  @NotNull
  public static String format(final long time) {
    final long differenceTime = System.currentTimeMillis() - time;

    final long days = TimeUnit.MILLISECONDS.toDays(differenceTime);
    final long hours = TimeUnit.MILLISECONDS.toHours(differenceTime) - TimeUnit.MILLISECONDS.toDays(differenceTime) * 24;
    final long minutes = TimeUnit.MILLISECONDS.toMinutes(differenceTime) - TimeUnit.MILLISECONDS.toHours(differenceTime) * 60;
    final long seconds = TimeUnit.MILLISECONDS.toSeconds(differenceTime) - TimeUnit.MILLISECONDS.toMinutes(differenceTime) * 60;

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
