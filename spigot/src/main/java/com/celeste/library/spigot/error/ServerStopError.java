package com.celeste.library.spigot.error;

public class ServerStopError extends Error {

  public ServerStopError(final String error) {
    super(error);
  }

  public ServerStopError(final Throwable cause) {
    super(cause);
  }

  public ServerStopError(final String error, final Throwable cause) {
    super(error, cause);
  }

}
