package com.celeste.library.core.util.formatter;

import com.celeste.library.core.util.pattern.RegexPattern;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;

public final class StringFormatter {

  public static String join(final String[] args) {
    final StringJoiner joiner = new StringJoiner(" ");
    Arrays.stream(args).forEach(joiner::add);

    return joiner.toString();
  }

  public static <T> String join(final List<T> args) {
    final StringJoiner joiner = new StringJoiner(" ");
    args.forEach(t -> joiner.add((CharSequence) t));

    return joiner.toString();
  }

  public static String join(final String[] args, final String format) {
    final StringJoiner joiner = new StringJoiner(format);
    Arrays.stream(args).forEach(joiner::add);

    return joiner.toString();
  }

  public static String join(final String[] args, final String beforeArg, final String afterArg) {
    final StringJoiner joiner = new StringJoiner(afterArg);
    Arrays.stream(args)
        .forEach(arg -> joiner.add(beforeArg + arg));

    return joiner.toString();
  }

  public static String join(final String[] args, final int startArg, final String format) {
    final StringJoiner joiner = new StringJoiner(format);

    Arrays.asList(args)
        .subList(startArg, args.length)
        .forEach(joiner::add);

    return joiner.toString();
  }

  public static String remove(final String string, final RegexPattern pattern) {
    return string.replaceAll(pattern.getPattern(), "");
  }

  public static String remove(final String string, final int size) {
    return string.substring(0, string.length() - size);
  }

  public static String startAtIndex(final String string, final int index) {
    return string.substring(index);
  }

  public static String[] split(final String string, final RegexPattern pattern) {
    return string.split(pattern.getPattern());
  }

}
