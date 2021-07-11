package com.celeste.library.core.util.formatter;

import com.celeste.library.core.util.formatter.type.TimeLanguage;
import com.celeste.library.core.util.formatter.type.TimeType;
import com.celeste.library.core.util.pattern.RegexPattern;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateFormatter {

  private static final SimpleDateFormat DATE_FORMAT;

  static {
    DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm");
    DATE_FORMAT.setTimeZone(TimeZone.getDefault());
  }

  public static String convertToString(final long time, final SimpleDateFormat dateFormat) {
    return dateFormat.format(time);
  }

  public static String convertToString(final long time) {
    return DATE_FORMAT.format(time);
  }

  public static String convertToString(final Date date, final SimpleDateFormat dateFormat) {
    return dateFormat.format(date.getTime());
  }

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

  public static String format(final long time) {
    return format(time, TimeLanguage.ENGLISH);
  }

  private static String format(final String str) {
    return " " + str;
  }

  private static String and(final TimeLanguage language) {
    return " " + language.getAnd() + " ";
  }

}
