package com.celeste.library.core.util.formatter;

import java.util.Arrays;
import java.util.StringJoiner;

public final class StringFormatter {

  public static String join(final String[] args) {
    final StringJoiner joiner = new StringJoiner(" ");
    Arrays.stream(args).forEach(joiner::add);

    return joiner.toString();
  }

  public static String joinWithFormat(final String[] args, final String format) {
    final StringJoiner joiner = new StringJoiner(format);
    Arrays.stream(args).forEach(joiner::add);

    return joiner.toString();
  }

  public static String joinWithArg(final String[] args, final int startArg, final String format) {
    final StringJoiner joiner = new StringJoiner(format);

    Arrays.asList(args)
        .subList(startArg, args.length)
        .forEach(joiner::add);

    return joiner.toString();
  }

  public static String remove(final String string, final int size) {
    return string.substring(0, string.length() - size);
  }

}
