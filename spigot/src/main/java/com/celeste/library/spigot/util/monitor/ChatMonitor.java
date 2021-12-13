package com.celeste.library.spigot.util.monitor;

import com.celeste.library.core.model.registry.Registry;
import com.celeste.library.core.model.registry.impl.ConcurrentRegistry;
import java.util.UUID;
import java.util.function.Consumer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class ChatMonitor {

  public static final Registry<UUID, ChatMonitor> MAP;

  static {
    MAP = new ConcurrentRegistry<>();
  }

  private UUID playerId;

  private Consumer<String> message;
  private Consumer<Void> cancel;

  public void register() {
    MAP.register(playerId, this);
  }

}
