package com.celeste.library.core.util.formatter;

import com.celeste.library.core.util.formatter.type.TimeLanguage;
import com.celeste.library.core.util.formatter.type.TimeType;
import com.celeste.library.core.util.pattern.RegexPattern;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.util.concurrent.TimeUnit.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateFormatter {

  private static final DateTimeFormatter FORMATTER;
  private static final ZoneId ZONE_ID;

  static {
    FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
    ZONE_ID = ZoneId.systemDefault();

    FORMATTER.withZone(ZONE_ID);
  }

  public static String convertToString(final long time, final DateTimeFormatter dateFormat) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZONE_ID)
        .format(dateFormat);
  }

  public static String convertToString(final long time) {
    return convertToString(time, FORMATTER);
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

      totalTime += TimeType.getType(prefix).getMultiplier() * time;
    }

    return totalTime;
  }

  /**
   * Formats the time to the format 7 days, 7 seconds, etc.
   *
   * @param time long
   * @return String
   */
  public static String format(final long time, final TimeLanguage language) {
    final long days = DAYS.convert(time, MILLISECONDS);
    final long hours = HOURS.convert(time, MILLISECONDS) - days * 24;
    final long minutes = MINUTES.convert(time, MILLISECONDS) - hours * 60;
    final long seconds = SECONDS.convert(time, MILLISECONDS) - minutes * 60;

    final StringBuilder builder = new StringBuilder();

    if (days > 0) {
      builder.append(days);
      builder.append(days == 1 ? format(language.getDay()) : format(language.getDays()));
    }

    if (hours > 0) {
      builder.append(days > 0 && (minutes > 0 || seconds > 0) ? ", " : and(language));
      builder.append(hours);
      builder.append(hours == 1 ? format(language.getHour()) : format(language.getHours()));
    }

    if (minutes > 0) {
      builder.append(days > 0 || hours > 0 && (seconds > 0) ? ", " : and(language));
      builder.append(minutes);
      builder.append(minutes == 1 ? format(language.getMinute()) : format(language.getMinutes()));
    }

    if (seconds > 0) {
      builder.append(days > 0 || hours > 0 || minutes > 0 ? and(language) : builder.length() > 0 ? ", "
          : "");
      builder.append(seconds);
      builder.append(seconds == 1 ? format(language.getSecond()) : format(language.getSeconds()));
    }

    return builder.toString();
  }

  /**
   * Formats the long time into string formatted to 00:00:00
   * like a clock, if the hours equals to 0 and withHours is false,
   * the clock is only displayed as 00:00
   *
   * @param time long time
   * @return String formatted
   */
  public static String format(final long time, final boolean withHours) {
    final long seconds = time / 1000;
    final long minutes = seconds / 60;
    final long hours = minutes / 60;

    final long newSeconds = seconds - minutes * 60;
    final String secondsString = newSeconds > 9 ? String.valueOf(newSeconds) : "0" + newSeconds;

    if (hours == 0 && !withHours) {
      final String minutesString = minutes > 9 ? String.valueOf(minutes) : "0" + minutes;
      return minutesString + ":" + secondsString;
    }

    final long newMinutes = minutes - hours * 60;
    final String minutesString = newMinutes > 9 ? String.valueOf(newMinutes) : "0" + newMinutes;

    final String hoursString = hours > 9 ? String.valueOf(hours) : "0" + hours;
    return hoursString + ":" + minutesString + ":" + secondsString;
  }

  public static String format(final long time) {
    return format(time, TimeLanguage.ENGLISH);
  }

  private static String format(final String string) {
    return " " + string;
  }

  private static String and(final TimeLanguage language) {
    return " " + language.getAnd() + " ";
  }

}
