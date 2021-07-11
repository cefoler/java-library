package com.celeste.library.core.util.pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.regex.Pattern;

@Getter
@RequiredArgsConstructor
public enum RegexPattern {

  UPPER_LETTERS("[A-Z]"),
  LOWER_LETTERS("[a-z]"),
  LETTERS("[A-Z|a-z]"),
  NUMBERS("[0-9]"),
  SPECIAL_LETTERS("[!@#$%^&*]"),
  REMOVE_GROUP("?:");

  private final String pattern;

  /**
   * Returns a pattern that gets only the String
   * by the start position to the
   * end position.
   */
  public static String fromLimit(final int start, final int end) {
    return "{" + start + "," + end + "}";
  }

  public static String amount(final int amount) {
    return "{" + amount + "}";
  }

  /**
   * Returns a pattern that gets only
   * the characters after the char provided
   */
  public static String startingFrom(final char str) {
    return "^([^" + str + "])+";
  }

  public static Pattern from(final RegexPattern pattern) {
    return Pattern.compile(pattern.getPattern());
  }

  public static boolean matches(final String str, final RegexPattern pattern) {
    return str.matches(pattern.getPattern());
  }

}
