package com.celeste.library.core.util.formatter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RomanFormatter {

  M("M", 1000),
  CM("CM", 900),
  D("D", 500),
  CD("CD", 400),
  C("C", 100),
  XC("XC", 90),
  L("XL", 50),
  XL("XL", 40),
  X("X", 10),
  IX("IX", 9),
  V("V", 5),
  IV("IV", 4),
  I("I", 1);

  private final String roman;
  private final int arabic;

  public static String serialize(final int arabic) {
    for (final RomanFormatter type : values()) {
      if (arabic < type.getArabic()) {
        continue;
      }

      final int newArabic = arabic - type.getArabic();
      return type.getRoman() + serialize(newArabic);
    }

    return "";
  }

  public static int deserialize(final String roman) {
    for (final RomanFormatter type : values()) {
      if (!roman.startsWith(type.getRoman())) {
        continue;
      }

      final String newRoman = roman.replaceFirst(type.getRoman(), "");
      return type.getArabic() + deserialize(newRoman);
    }

    return 0;
  }

}
