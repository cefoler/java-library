package com.celeste.library.spigot.model.menu.entity.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class InventoryRenderEvent extends Event {

  private final HandlerList handlerList;

  public InventoryRenderEvent() {
    this.handlerList = new HandlerList();
  }

  @Override
  public HandlerList getHandlers() {
    return handlerList;
  }

}
