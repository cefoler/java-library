package com.celeste.library.core.util.matcher;

import com.celeste.library.core.util.pattern.RegexPattern;

public final class StringMatcher {

  public static boolean contains(final String str, final String part){
    return str.matches(".*" + part + ".*");
  }

  public boolean containsLetters(final String str) {
    return str.matches(RegexPattern.LETTERS.getPattern());
  }

  public boolean containsLetters(final String str, final int amount) {
    return str.matches("[a-zA-Z]*" + amount + "}");
  }

}
