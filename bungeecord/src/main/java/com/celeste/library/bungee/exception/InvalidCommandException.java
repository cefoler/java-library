package com.celeste.library.bungee.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidCommandException extends RuntimeException {

  public InvalidCommandException(@NotNull final String error) {
    super(error);
  }

  public InvalidCommandException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
