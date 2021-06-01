package com.celeste.library.spigot.model.menu.entity.impl;

import com.celeste.library.spigot.model.menu.MenuHolder;
import com.celeste.library.spigot.model.menu.entity.AbstractContext;
import com.celeste.library.spigot.model.menu.entity.event.InventoryRenderEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryOpenEvent;

public final class OpenContext extends AbstractContext<InventoryOpenEvent> {

  public OpenContext(Player player, InventoryOpenEvent context, MenuHolder holder) {
    super(player, context, holder);
  }

}
