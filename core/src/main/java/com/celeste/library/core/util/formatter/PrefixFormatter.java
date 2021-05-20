package com.celeste.library.core.util.formatter;

import java.text.DecimalFormat;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PrefixFormatter {

  K("K", 1),
  M("M", 2),
  B("B", 3),
  T("T", 4),
  Q("Q", 5),
  QQ("QQ", 6),
  S("S", 7),
  SS("SS", 8),
  O("O", 9),
  N("N", 10),
  D("D", 11);

  private final String prefix;
  private final int multiplier;

  public static <T extends Number> String serialize(final T number) {
    final PrefixFormatter[] prefixes = values();
    final DecimalFormat decimal = new DecimalFormat("#.##");

    double value = Double.parseDouble(String.valueOf(number));
    int index = -1;

    for (;value / 1000 >= 1; value /= 1000) {
      index++;
    }

    if (index == -1) {
      return decimal.format(value);
    }

    value = Math.round(value * 100.0) / 100.0;

    return String.valueOf(value) + prefixes[index];
  }

  public static double deserialize(final String number) {
    final PrefixFormatter prefix = Arrays.stream(values())
        .filter(type -> number.contains(type.getPrefix()))
        .findFirst()
        .orElse(null);

    if (prefix == null) {
      return Double.parseDouble(number);
    }

    final String numbers = number.replaceAll("[A-Z|a-z]", "");
    final double value = Double.parseDouble(numbers);

    return value * Math.pow(1000, prefix.getMultiplier());
  }

}
