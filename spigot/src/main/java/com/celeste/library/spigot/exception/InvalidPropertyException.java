package com.celeste.library.spigot.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidPropertyException extends RuntimeException {

  public InvalidPropertyException(@NotNull final String error) {
    super(error);
  }

  public InvalidPropertyException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
