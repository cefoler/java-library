package com.celeste.library.core.util.formatter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

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
    return Arrays.stream(values())
        .filter(type -> type.getArabic() < arabic)
        .map(type -> type.getRoman() + serialize(arabic - type.getArabic()))
        .findFirst()
        .orElse("");
  }

  public static int deserialize(final String roman) {
    return Arrays.stream(values())
        .filter(type -> roman.startsWith(type.getRoman()))
        .map(type -> type.getArabic() + deserialize(roman.replaceFirst(type.getRoman(), "")))
        .findFirst()
        .orElse(0);
  }

}
