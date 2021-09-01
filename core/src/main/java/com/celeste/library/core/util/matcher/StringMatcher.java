package com.celeste.library.core.util.matcher;

import com.celeste.library.core.util.builder.RegexBuilder;
import com.celeste.library.core.util.pattern.RegexPattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.celeste.library.core.util.pattern.RegexPattern.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringMatcher {

  public static boolean matches(final String str, final RegexPattern pattern) {
    return Pattern.matches(pattern.getPattern(), str);
  }

  public static boolean matches(final String str, final String pattern) {
    return Pattern.matches(pattern, str);
  }

  public static boolean matches(final String str, final Pattern pattern) {
    return Pattern.matches(pattern.pattern(), str);
  }

  public static Matcher matcher(final String str, final String pattern) {
    return Pattern.compile(pattern).matcher(str);
  }

  public static Matcher matcher(final String str, final RegexPattern pattern) {
    return Pattern.compile(pattern.getPattern()).matcher(str);
  }

  public static boolean contains(final String str, final String part) {
    return Pattern.matches(RegexPattern.contains(part), part);
  }

  public boolean containsLetters(final String str) {
    return Pattern.matches(str, LETTERS.getPattern());
  }

  public boolean containsLetters(final String str, final int amount) {
    return Pattern.matches(RegexBuilder.builder()
        .append(LETTERS)
        .amount(amount)
        .buildAsString(), str);
  }

  public boolean containsNumbers(final String str) {
    return Pattern.matches(NUMBERS.getPattern(), str);
  }

  public boolean containsSpecialLetters(final String str) {
    return Pattern.matches(SPECIAL_LETTERS.getPattern(), str);
  }

  public boolean isHexaDecimal(final String str) {
    return Pattern.matches(HEXADECIMAL.getPattern(), str);
  }

}
