package com.celeste.library.core.util.matcher;

import com.celeste.library.core.util.pattern.RegexPattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.celeste.library.core.util.pattern.RegexPattern.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringMatcher {

  public static boolean matches(final String string, final RegexPattern pattern) {
    return Pattern.matches(pattern.getPattern(), string);
  }

  public static boolean matches(final String string, final String pattern) {
    return Pattern.matches(pattern, string);
  }

  public static boolean matches(final String string, final Pattern pattern) {
    return Pattern.matches(pattern.pattern(), string);
  }

  public static Matcher matcher(final String string, final String pattern) {
    return Pattern.compile(pattern).matcher(string);
  }

  public static Matcher matcher(final String string, final RegexPattern pattern) {
    return Pattern.compile(pattern.getPattern()).matcher(string);
  }

  public static boolean contains(final String string, final String part) {
    return Pattern.matches(RegexPattern.contains(part), string);
  }

  public boolean containsLetters(final String string) {
    return Pattern.matches(string, LETTERS.getPattern());
  }

  public boolean containsNumbers(final String string) {
    return Pattern.matches(NUMBERS.getPattern(), string);
  }

  public boolean containsSpecialLetters(final String string) {
    return Pattern.matches(SPECIAL_LETTERS.getPattern(), string);
  }

  public boolean isHexaDecimal(final String string) {
    return Pattern.matches(HEXADECIMAL.getPattern(), string);
  }

}
