package com.celeste.library.core.util.formatter;

import java.text.DecimalFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberFormatter {

  private static final String[] SUFFIXES = new String[]{"", "K", "M", "B", "T", "Q", "QQ", "S"};

  public static String format(float value) {
    int index = 0;

    float formattedValue;
    while ((formattedValue = value / 1000) >= 1) {
      value = formattedValue;
      index++;
    }

    final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    return decimalFormat.format(value) + SUFFIXES[index];
  }

}