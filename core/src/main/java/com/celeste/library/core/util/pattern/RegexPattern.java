package com.celeste.library.core.util.pattern;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

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

  /**
   * Returns a pattern that gets only
   * the characters after the char provided
   */
  public static String startingFrom(final char str) {
    return "^([^" + str + "])+";
  }

}
