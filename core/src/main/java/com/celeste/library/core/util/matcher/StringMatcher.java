package com.celeste.library.core.util.matcher;

import com.celeste.library.core.util.builder.RegexBuilder;
import com.celeste.library.core.util.pattern.RegexPattern;

import java.util.regex.Pattern;

public final class StringMatcher {

  public static boolean matches(final String str, final RegexPattern pattern){
    return Pattern.matches(pattern.getPattern(), str);
  }

  public static boolean matches(final String str, final String pattern){
    return Pattern.matches(pattern, str);
  }

  public static boolean matches(final String str, final Pattern pattern){
    return Pattern.matches(pattern.pattern(), str);
  }

  public static boolean contains(final String str, final String part){
    return Pattern.matches(RegexPattern.contains(part), part);
  }

  public boolean containsLetters(final String str) {
    return Pattern.matches(str, RegexPattern.LETTERS.getPattern());
  }

  public boolean containsLetters(final String str, final int amount) {
    return Pattern.matches(RegexBuilder.builder()
        .append(RegexPattern.LETTERS)
        .amount(amount)
        .buildAsString(), str);
  }

  public boolean containsNumbers(final String str) {
    return Pattern.matches(RegexPattern.NUMBERS.getPattern(), str);
  }

  public boolean isHexaDecimal(final String str) {
    return Pattern.matches(RegexPattern.HEXADECIMAL.getPattern(), str);
  }

}
