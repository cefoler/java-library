package com.celeste.library.spigot.model.menu.entity.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.menu.entity.AbstractContext;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public final class DragContext extends AbstractContext<InventoryDragEvent> {

  public DragContext(Player player, InventoryDragEvent context, MenuHolder holder) {
    super(player, context, holder);
  }

}
