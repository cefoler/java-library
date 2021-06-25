package com.celeste.library.spigot.util.monitor;

import com.celeste.library.core.model.registry.Registry;
import com.celeste.library.core.model.registry.impl.ConcurrentRegistry;
import lombok.Data;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Data
public final class ChatMonitor {

  public static final Registry<UUID, ChatMonitor> MAP;

  static {
    MAP = new ConcurrentRegistry<>();
  }

  private UUID playerId;

  private Consumer<String> message;
  private Consumer<Void> cancel;

  private ChatMonitor player(final UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  private ChatMonitor message(final Consumer<String> message) {
    this.message = message;
    return this;
  }

  private ChatMonitor cancel(final Consumer<Void> cancel) {
    this.cancel = cancel;
    return this;
  }

  private void build() {
    MAP.register(playerId, this);
  }

}
