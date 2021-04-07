package com.celeste.library.shared.util;

import java.util.StringJoiner;

public final class StringUtil {

  /**
   * Join arguments to form a phrase with a
   * space between each argument
   * @param args String
   *
   * @return String formatted phrase
   */
  public static String join(final String[] args) {
    final StringJoiner joiner = new StringJoiner(" ");
    for (String arg : args) {
      joiner.add(arg);
    }

    return joiner.toString();
  }

  /**
   * Join the arguments with the format specified
   * between each argument
   * @param args String[]
   * @param format String
   *
   * @return String
   */
  public static String joinWithFormat(final String[] args, final String format) {
    final StringJoiner joiner = new StringJoiner(format);
    for (String arg : args) {
      joiner.add(arg);
    }

    return joiner.toString();
  }

}
