package com.celeste.library.spigot.util.monitor;

import com.celeste.library.core.model.registry.Registry;
import com.celeste.library.core.model.registry.impl.ConcurrentRegistry;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public final class ChatMonitor {

  public static final Registry<UUID, ChatMonitor> MAP;

  static {
    MAP = new ConcurrentRegistry<>();
  }

  private UUID playerId;

  private Consumer<String> message;
  private Consumer<Void> cancel;

  public static ChatMonitor builder() {
    return new ChatMonitor();
  }

  public ChatMonitor player(@NotNull final UUID playerId) {
    this.playerId = playerId;
    return this;
  }

  public ChatMonitor message(@NotNull final Consumer<String> message) {
    this.message = message;
    return this;
  }

  public ChatMonitor cancel(@NotNull final Consumer<Void> cancel) {
    this.cancel = cancel;
    return this;
  }

  public ChatMonitor build() {
    MAP.register(playerId, this);
    return this;
  }

}
