package com.celeste.library.core.util;

import com.google.common.flogger.FluentLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Logger {

  private static final FluentLogger FLUENT_LOGGER;

  static {
    FLUENT_LOGGER = FluentLogger.forEnclosingClass();
  }

  public static FluentLogger getLogger() {
    return FLUENT_LOGGER;
  }

}
