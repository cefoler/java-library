package com.celeste.library.core.util.builder;

import com.celeste.library.core.model.registry.type.KeyType;
import com.celeste.library.core.util.formatter.StringFormatter;
import com.celeste.library.core.util.pattern.RegexPattern;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class StringBuilder {

  private String value;
  private KeyType keyType;

  public StringBuilder(final String string) {
    this.value = string;
    this.keyType = KeyType.STANDARD;
  }

  public StringBuilder append(final String arg) {
    this.value = value + arg;
    return this;
  }

  public StringBuilder append(final String[] args, final String format) {
    this.value = this.value + StringFormatter.join(args, format);
    return this;
  }

  public StringBuilder append(final String format, final String... args) {
    this.value = this.value + StringFormatter.join(args, format);
    return this;
  }

  public StringBuilder append(final String[] args, final String beforeArg, final String afterArg) {
    this.value = value + StringFormatter.join(args, beforeArg, afterArg);
    return this;
  }

  public StringBuilder append(final String beforeArg, final String afterArg, final String... args) {
    this.value = value + StringFormatter.join(args, beforeArg, afterArg);
    return this;
  }

  public StringBuilder remove(final RegexPattern pattern) {
    this.value = value.replaceAll(pattern.getPattern(), "");
    return this;
  }

  public StringBuilder remove(final int size) {
    this.value = value.substring(0, value.length() - size);
    return this;
  }

  public StringBuilder startAtIndex(final int index) {
    this.value = StringFormatter.startAtIndex(value, index);
    return this;
  }

  public StringBuilder onlyWith(final RegexPattern pattern) {
    this.value = value.replaceAll("(" + RegexPattern.REMOVE_GROUP + pattern.getPattern() + ")", "");
    return this;
  }

  public StringBuilder lowerCase() {
    this.keyType = KeyType.LOWER_CASE;
    return this;
  }

  public StringBuilder upperCase() {
    this.keyType = KeyType.UPPER_CASE;
    return this;
  }

  public StringBuilder standard() {
    this.keyType = KeyType.STANDARD;
    return this;
  }

  public String build() {
    return keyType.convert(value);
  }

}
