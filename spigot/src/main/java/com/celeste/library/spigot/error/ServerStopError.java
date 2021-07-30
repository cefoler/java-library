package com.celeste.library.spigot.error;

import org.bukkit.Bukkit;

public class ServerStopError extends Error {

  public ServerStopError(final String error) {
    super(error);
    Bukkit.shutdown();
  }

  public ServerStopError(final Throwable cause) {
    super(cause);
    Bukkit.shutdown();
  }

  public ServerStopError(final String error, final Throwable cause) {
    super(error, cause);
    Bukkit.shutdown();
  }

}
