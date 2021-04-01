package com.celeste.library.bungee.exception;

import org.jetbrains.annotations.NotNull;

public class InvalidListenerException extends RuntimeException {

  public InvalidListenerException(@NotNull final String error) {
    super(error);
  }

  public InvalidListenerException(@NotNull final String error, Throwable cause) {
    super(error, cause);
  }

}
