package com.celeste.library.spigot.view.event.wrapper;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;

public abstract class AbstractEvent extends Event {

  private static final HandlerList HANDLER_LIST;
  private static final PluginManager MANAGER;

  static {
    HANDLER_LIST = new HandlerList();
    MANAGER = Bukkit.getPluginManager();
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public void dispatch() {
    MANAGER.callEvent(this);
  }

}
