package com.celeste.library.core.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RegexPattern {

  REMOVE_UPPER_LETTERS("[A-Z]"),
  REMOVE_LOWER_LETTERS("[a-z]"),
  REMOVE_LETTERS("[A-Z|a-z]"),
  REMOVE_NUMBERS("[0-9]");

  private final String pattern;

}
