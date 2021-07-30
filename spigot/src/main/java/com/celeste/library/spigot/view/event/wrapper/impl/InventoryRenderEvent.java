package com.celeste.library.spigot.view.event.wrapper.impl;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

@Getter
public final class InventoryRenderEvent extends InventoryEvent {

  private final Player player;
  private final Inventory inventory;

  public InventoryRenderEvent(final Player player, final Inventory inventory) {
    super(null);

    this.player = player;
    this.inventory = inventory;
  }

}
