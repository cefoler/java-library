package com.celeste.library.spigot.view.event.wrapper;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public abstract class AbstractEvent extends Event {

  private static final HandlerList HANDLER_LIST;

  static {
    HANDLER_LIST = new HandlerList();
  }

  /**
   * @return HandlerList
   */
  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  /**
   * Static method to get the HandlerList
   *
   * @return HandlerList
   */
  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

}
