package com.celeste.library.spigot.util.monitor;

import lombok.Builder;
import lombok.Data;
import org.bukkit.Bukkit;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Data
@Builder
public final class ChatMonitor {

  public static final Map<UUID, ChatMonitor> MAP;

  static {
    MAP = new ConcurrentHashMap<>();
  }

  private final UUID playerId;

  private final Consumer<String> message;
  private final Consumer<Void> cancel;

}
