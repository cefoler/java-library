package com.celeste.library.spigot.exception;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class PlayerNotFoundException extends IOException {

  /**
   * @param error String
   */
  public PlayerNotFoundException(@NotNull final String error) {
    super(error);
  }

  /**
   * @param cause Throwable
   */
  public PlayerNotFoundException(@NotNull final Throwable cause) {
    super(cause);
  }

  /**
   * @param error String
   * @param cause Throwable
   */
  public PlayerNotFoundException(@NotNull final String error, @NotNull final Throwable cause) {
    super(error, cause);
  }

}
