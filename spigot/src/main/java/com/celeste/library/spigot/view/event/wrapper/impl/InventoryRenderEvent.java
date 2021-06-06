package com.celeste.library.spigot.view.event.wrapper.impl;

import com.celeste.library.spigot.view.event.wrapper.AbstractCancellableEvent;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@Getter
@RequiredArgsConstructor
public final class InventoryRenderEvent extends AbstractCancellableEvent {

  private final Player player;
  private final Inventory inventory;

}
