package com.celeste.library.core.util;

import com.google.common.flogger.FluentLogger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Logger {

  @Getter
  private static final FluentLogger logger;

  static {
    logger = FluentLogger.forEnclosingClass();
  }

}
