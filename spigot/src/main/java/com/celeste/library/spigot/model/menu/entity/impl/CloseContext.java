package com.celeste.library.spigot.model.menu.entity.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.menu.entity.AbstractContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public final class CloseContext extends AbstractContext<InventoryCloseEvent> {

  public CloseContext(Player player, InventoryCloseEvent context, MenuHolder holder) {
    super(player, context, holder);
  }

}
