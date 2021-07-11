package com.celeste.library.core.util.builder;

import com.celeste.library.core.util.pattern.RegexPattern;
import lombok.AllArgsConstructor;

import java.util.regex.Pattern;

@AllArgsConstructor
public final class RegexBuilder {

  private String pattern;

  public static RegexBuilder builder() {
    return new RegexBuilder("");
  }

  public RegexBuilder limit(final int start, final int end) {
    pattern = pattern + RegexPattern.fromLimit(start, end);
    return this;
  }

  public RegexBuilder amount(final int amount) {
    pattern = pattern + RegexPattern.amount(amount);
    return this;
  }

  public RegexBuilder startsFrom(final char from) {
    pattern = pattern + RegexPattern.startingFrom(from);
    return this;
  }

  public String buildAsString() {
    return pattern;
  }

  public Pattern build() {
    return Pattern.compile(pattern);
  }

}
