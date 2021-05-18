package com.celeste.library.core.util.formatter;

import java.util.Arrays;
import java.util.StringJoiner;

public final class StringFormatter {

  /**
   * Join arguments to form a phrase with a space between each argument
   *
   * @param args String
   * @return String formatted phrase
   */
  public static String join(final String[] args) {
    final StringJoiner joiner = new StringJoiner(" ");
    Arrays.stream(args).forEach(joiner::add);

    return joiner.toString();
  }

  /**
   * Join the arguments with the format specified between each argument
   *
   * @param args   String[]
   * @param format String
   * @return String
   */
  public static String joinWithFormat(final String[] args, final String format) {
    final StringJoiner joiner = new StringJoiner(format);
    Arrays.stream(args).forEach(joiner::add);

    return joiner.toString();
  }

  /**
   * Join the arguments from the array, it begins in the startArg provided.
   *
   * @param args     String[]
   * @param startArg int
   * @param format   String
   * @return String
   */
  public static String joinWithArg(final String[] args, final int startArg, final String format) {
    final StringJoiner joiner = new StringJoiner(format);
    for (int index = startArg; index < args.length; index++) {
      joiner.add(args[index]);
    }

    return joiner.toString();
  }

  /**
   * Returns a String removing a size of arguments from the original String
   *
   * @param str  String
   * @param size Integer
   * @return String
   */
  public static String remove(final String str, final int size) {
    return str.substring(0, str.length() - size);
  }

}
