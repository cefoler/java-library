package com.celeste.library.core.util.formatter.type;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

@Getter
@AllArgsConstructor
public enum TimeType {

  SECONDS(1000, "S", "SECOND", "SECONDS"),
  MINUTES(60 * 1000, "M", "MI", "MT", "MINUTE", "MINUTES"),
  HOURS(60 * 60 * 1000, "H", "HOUR", "HOURS"),
  DAYS(24 * 60 * 60 * 1000, "D", "DAY", "DAYS"),
  WEEKS(7 * 24 * 60 * 60 * 1000, "W", "WEEK", "WEEKS"),
  MONTHS(30L * 24 * 60 * 60 * 1000, "MM", "MO", "MONTH", "MONTHS"),
  YEARS(365L * 24 * 60 * 60 * 1000, "Y", "YEAR", "YEARS");

  private final long multiplier;
  private final List<String> names;

  TimeType(final long multiplier, final String... names) {
    this.multiplier = multiplier;
    this.names = Arrays.asList(names);
  }

  public static TimeType getType(final String time) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(time.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid time: " + time));
  }

  public static TimeType getType(final String time, @Nullable final TimeType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(time.toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

  public static TimeType getType(final char time) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(Character.toString(time).toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Invalid time: " + time));
  }

  public static TimeType getType(final char time, @Nullable final TimeType orElse) {
    return Arrays.stream(values())
        .filter(type -> type.getNames().contains(Character.toString(time).toUpperCase()))
        .findFirst()
        .orElse(orElse);
  }

}
