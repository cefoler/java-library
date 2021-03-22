package com.celeste.util.formatter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RomanNumberFormatter {

  @Getter
  private static final RomanNumberFormatter instance = new RomanNumberFormatter();

  private final String[] symbols = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
  private final int[] numbers = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };

  public String format(int number) {
    for (int i = 0; i < numbers.length; i++) {
      if (number >= numbers[i]) {
        return symbols[i] + format(number - numbers[i]);
      }
    }
    return "";
  }

  public int unformat(String number) {
    for (int i = 0; i < symbols.length; i++) {
      if (!number.startsWith(symbols[i])) continue;

      return numbers[i] + unformat(number.replaceFirst(symbols[i], ""));
    }
    return 0;
  }

}
