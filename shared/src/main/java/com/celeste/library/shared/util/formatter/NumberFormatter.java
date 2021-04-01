package com.celeste.library.shared.util.formatter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberFormatter {

  private static final String[] suffixes = new String[]{ "", "K", "M", "B", "T", "Q", "QQ", "S" };

  @NotNull
  public static String format(float value) {
    int index = 0;

    float tmp;
    while ((tmp = value / 1000) >= 1) {
      value = tmp;
      ++index;
    }

    final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    return decimalFormat.format(value) + suffixes[index];
  }

}