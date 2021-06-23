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
  SPECIAL_LETTERS("/[!@#$%^&*]/g");

  private final String pattern;

}
