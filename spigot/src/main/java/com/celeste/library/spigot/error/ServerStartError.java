package com.celeste.library.spigot.error;

import org.bukkit.Bukkit;

public class ServerStartError extends Error {

  public ServerStartError(final String error) {
    super(error);
    Bukkit.shutdown();
  }

  public ServerStartError(final Throwable cause) {
    super(cause);
    Bukkit.shutdown();
  }

  public ServerStartError(final String error, final Throwable cause) {
    super(error, cause);
    Bukkit.shutdown();
  }

}
