package com.celeste.library.core.util.formatter.type;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public enum TimeMultiplier {

  SECONDS(1000, "S", "SECOND", "SECONDS"),
  MINUTES(60 * 1000, "M", "MI", "MT", "MINUTE", "MINUTES"),
  HOURS(60 * 60 * 1000, "H", "HOUR", "HOURS"),
  DAYS(24 * 60 * 60 * 1000, "D", "DAY", "DAYS"),
  WEEKS(7 * 24 * 60 * 60 * 1000, "W", "WEEK", "WEEKS"),
  MONTHS(30L * 24 * 60 * 60 * 1000, "MM", "MO", "MONTH", "MONTHS"),
  YEARS(365L * 24 * 60 * 60 * 1000, "Y", "YEAR", "YEARS");

  private final long multiplier;
  private final List<String> prefixes;

  TimeMultiplier(final long multiplier, @NotNull final String... prefixes) {
    this.multiplier = multiplier;
    this.prefixes = Arrays.asList(prefixes);
  }

  /**
   * Returns the time according to its name
   *
   * @param prefix String
   * @return TimeMultiplierType
   */
  public static TimeMultiplier getType(final String prefix) {
    return Arrays.stream(values())
        .filter(type -> type.getPrefixes().contains(prefix.toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Argument " + prefix + " isn't valid"));
  }

  public static TimeMultiplier getType(final char prefix) {
    return Arrays.stream(values())
        .filter(type -> type.getPrefixes().contains(Character.toString(prefix).toUpperCase()))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Argument " + prefix + " isn't valid"));
  }

}
