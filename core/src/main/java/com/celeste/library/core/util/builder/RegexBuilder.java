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

  public RegexBuilder append(final RegexPattern pattern) {
    this.pattern = pattern + pattern.getPattern();
    return this;
  }

  public RegexBuilder append(final String pattern) {
    this.pattern = pattern + pattern;
    return this;
  }

  public RegexBuilder limit(final int start, final int end) {
    this.pattern = pattern + RegexPattern.limit(start, end);
    return this;
  }

  public RegexBuilder amount(final int amount) {
    this.pattern = pattern + RegexPattern.amount(amount);
    return this;
  }

  public RegexBuilder startsFrom(final char from) {
    this.pattern = pattern + RegexPattern.startingFrom(from);
    return this;
  }

  public String buildAsString() {
    return pattern;
  }

  public Pattern build() {
    return Pattern.compile(pattern);
  }

}
