package com.celeste.util.formatter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberFormatter {

  @Getter
  private static final NumberFormatter instance = new NumberFormatter();

  private final String[] suffixes = new String[]{ "", "K", "M", "B", "T", "Q", "QQ", "S" };

  public String format(float value) {
    int index = 0;

    float tmp;
    while ((tmp = value / 1000) >= 1) {
      value = tmp;
      ++index;
    }

    final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    return decimalFormat.format(value) + this.suffixes[index];
  }

}