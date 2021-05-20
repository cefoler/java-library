package com.celeste.library.spigot.error;

public class ServerStartError extends Error {

  public ServerStartError(final String error) {
    super(error);
  }

  public ServerStartError(final Throwable cause) {
    super(cause);
  }

  public ServerStartError(final String error, final Throwable cause) {
    super(error, cause);
  }

}
